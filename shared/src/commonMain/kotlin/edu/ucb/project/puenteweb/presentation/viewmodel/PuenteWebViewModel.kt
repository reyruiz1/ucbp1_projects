package edu.ucb.project.puenteweb.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucb.project.catalogo.domain.usecase.GetMoviesUsecase
import edu.ucb.project.navigation.NavRoute
import edu.ucb.project.puenteweb.data.ProtocoloPuente
import edu.ucb.project.puenteweb.data.texto
import edu.ucb.project.puenteweb.domain.PreferenciasPuenteWeb
import edu.ucb.project.puenteweb.domain.model.Acciones
import edu.ucb.project.puenteweb.domain.model.MensajePuente
import edu.ucb.project.puenteweb.domain.model.PeliculaWeb
import edu.ucb.project.puenteweb.domain.model.TareaWeb
import edu.ucb.project.puenteweb.domain.model.TipoMensaje
import edu.ucb.project.tareas.domain.model.Tarea
import edu.ucb.project.tareas.domain.repository.TareasRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class PuenteWebViewModel(
    private val getMoviesUsecase: GetMoviesUsecase,
    private val tareasRepository: TareasRepository,
    private val preferencias: PreferenciasPuenteWeb
) : ViewModel() {

    private val _state = MutableStateFlow(
        PuenteWebState(url = preferencias.obtenerUrl(), modoOscuro = preferencias.obtenerModoOscuro())
    )
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<PuenteWebEffect>()
    val effect = _effect.asSharedFlow()

    private val _cartasSalientes = Channel<String>(Channel.BUFFERED)
    val cartasSalientes = _cartasSalientes.receiveAsFlow()

    private val ticketsPendientes = mutableMapOf<String, CompletableDeferred<MensajePuente>>()
    private var ultimoTicket = 0
    private var ultimaEntrada = 0
    private var ultimaLista: List<Tarea> = emptyList()

    init {
        viewModelScope.launch {
            tareasRepository.observarTareas()
                .catch { e -> anotar(Direccion.SISTEMA, "Error leyendo Firestore: ${e.message}") }
                .collect { lista ->
                    ultimaLista = lista
                    if (_state.value.conectado) enviarLista()
                }
        }
    }

    fun emitEvent(event: PuenteWebEvent) {
        when (event) {
            is PuenteWebEvent.OnCartaRecibida -> recibir(event.texto)
            PuenteWebEvent.OnCanalCerrado -> cerrarCanal()
            is PuenteWebEvent.OnModoOscuroChange -> cambiarModoOscuro(event.activo)
            PuenteWebEvent.OnPedirSeleccion -> pedirSeleccion()
            PuenteWebEvent.OnRecargar -> _state.update { it.copy(recargas = it.recargas + 1) }
            PuenteWebEvent.OnAbrirUrl -> _state.update {
                it.copy(editandoUrl = true, urlEditable = it.url, errorUrl = null)
            }
            is PuenteWebEvent.OnUrlChange -> _state.update {
                it.copy(urlEditable = event.url, errorUrl = null)
            }
            PuenteWebEvent.OnGuardarUrl -> guardarUrl()
            PuenteWebEvent.OnCerrarUrl -> _state.update { it.copy(editandoUrl = false) }
            PuenteWebEvent.OnCerrarAviso -> _state.update { it.copy(aviso = null) }
            PuenteWebEvent.OnLimpiarBitacora -> _state.update { it.copy(bitacora = emptyList()) }
            PuenteWebEvent.OnAlternarBitacora -> _state.update { it.copy(bitacoraVisible = !it.bitacoraVisible) }
        }
    }

    private fun recibir(texto: String) {
        val carta = ProtocoloPuente.leer(texto)
        if (carta == null) {
            anotar(Direccion.WEB_A_APP, "Carta rechazada, formato inválido: ${texto.take(60)}")
            return
        }
        anotar(Direccion.WEB_A_APP, describir(carta))
        when (carta.type) {
            TipoMensaje.EVENT -> atenderEvento(carta)
            TipoMensaje.REQUEST -> atenderPeticion(carta)
            TipoMensaje.RESPONSE -> {
                val ticket = ticketsPendientes.remove(carta.id.orEmpty())
                if (ticket == null) {
                    anotar(Direccion.SISTEMA, "Respuesta sin pregunta pendiente: #${carta.id}")
                } else {
                    ticket.complete(carta)
                }
            }
        }
    }

    private fun atenderEvento(carta: MensajePuente) {
        when (carta.action) {
            Acciones.LISTO -> {
                _state.update { it.copy(conectado = true) }
                anotar(Direccion.SISTEMA, "Canal abierto: la web saludó")
                enviarEvento(Acciones.CAMBIAR_TEMA, temaActual())
                enviarLista()
            }
            Acciones.ABRIR_PANTALLA -> abrirPantalla(carta.texto("pantalla"))
            Acciones.CAMBIAR_TEMA -> aplicarTemaDeLaWeb(carta.texto("tema"))
            else -> anotar(Direccion.SISTEMA, "Evento desconocido ignorado: ${carta.action}")
        }
    }

    private fun aplicarTemaDeLaWeb(tema: String?) {
        val oscuro = when (tema) {
            TEMA_OSCURO -> true
            TEMA_CLARO -> false
            else -> {
                anotar(Direccion.SISTEMA, "Tema no válido: $tema")
                return
            }
        }
        guardarModoOscuro(oscuro)
        anotar(Direccion.SISTEMA, "La web cambió el tema a $tema")
    }

    private fun abrirPantalla(pantalla: String?) {
        val ruta = when (pantalla) {
            "catalogo" -> NavRoute.Catalog
            "tareas" -> NavRoute.Tareas
            else -> null
        }
        if (ruta == null) {
            anotar(Direccion.SISTEMA, "Pantalla no permitida: $pantalla")
            return
        }
        viewModelScope.launch { _effect.emit(PuenteWebEffect.AbrirPantalla(ruta)) }
    }

    private fun atenderPeticion(carta: MensajePuente) {
        val id = carta.id
        if (id.isNullOrBlank()) {
            anotar(Direccion.SISTEMA, "Petición sin id ignorada: ${carta.action}")
            return
        }
        viewModelScope.launch {
            val respuesta = try {
                val datos = when (carta.action) {
                    Acciones.OBTENER_PELICULAS -> obtenerPeliculas()
                    Acciones.AGREGAR_A_LISTA -> agregarALista(carta.texto("titulo"))
                    else -> throw IllegalArgumentException("Acción desconocida: ${carta.action}")
                }
                MensajePuente(TipoMensaje.RESPONSE, carta.action, id, datos, ok = true)
            } catch (e: Exception) {
                MensajePuente(TipoMensaje.RESPONSE, carta.action, id, ok = false, error = e.message ?: "Error en la app")
            }
            enviar(respuesta)
        }
    }

    private suspend fun obtenerPeliculas(): JsonObject {
        val peliculas = getMoviesUsecase.invoke().getOrThrow()
            .filter { it.posterPath.isNotBlank() }
            .map { PeliculaWeb(titulo = it.title, poster = it.posterPath) }
        return ProtocoloPuente.empaquetar("peliculas", peliculas)
    }

    private suspend fun agregarALista(titulo: String?): JsonObject {
        val limpio = titulo?.trim().orEmpty()
        require(limpio.isNotEmpty()) { "El título está vacío" }
        require(limpio.length <= 80) { "El título es muy largo" }
        require(ultimaLista.none { it.titulo.trim().equals(limpio, ignoreCase = true) }) { "Ya está en tu lista" }
        tareasRepository.agregar(limpio)
        return buildJsonObject { put("titulo", limpio) }
    }

    private fun enviarLista() {
        val tareas = ultimaLista.map { TareaWeb(id = it.id, titulo = it.titulo, completada = it.completada) }
        enviarEvento(Acciones.LISTA_ACTUALIZADA, ProtocoloPuente.empaquetar("tareas", tareas))
    }

    private fun cambiarModoOscuro(activo: Boolean) {
        guardarModoOscuro(activo)
        enviarEvento(Acciones.CAMBIAR_TEMA, temaActual())
    }

    private fun guardarModoOscuro(activo: Boolean) {
        preferencias.guardarModoOscuro(activo)
        _state.update { it.copy(modoOscuro = activo) }
    }

    private fun temaActual(): JsonObject {
        return buildJsonObject { put("tema", if (_state.value.modoOscuro) TEMA_OSCURO else TEMA_CLARO) }
    }

    private fun pedirSeleccion() {
        if (!_state.value.conectado) {
            _state.update { it.copy(aviso = "La web todavía no está conectada") }
            return
        }
        viewModelScope.launch {
            val respuesta = preguntarALaWeb(Acciones.OBTENER_SELECCION)
            val aviso = when {
                respuesta == null -> "La web no respondió a tiempo"
                respuesta.ok == true -> "En la web elegiste: ${respuesta.texto("titulo")}"
                else -> respuesta.error ?: "La web no pudo responder"
            }
            _state.update { it.copy(aviso = aviso) }
        }
    }

    private suspend fun preguntarALaWeb(
        accion: String,
        payload: JsonObject = JsonObject(emptyMap())
    ): MensajePuente? {
        ultimoTicket++
        val id = "a-$ultimoTicket"
        val ticket = CompletableDeferred<MensajePuente>()
        ticketsPendientes[id] = ticket
        enviar(MensajePuente(TipoMensaje.REQUEST, accion, id, payload))
        val respuesta = withTimeoutOrNull(TIEMPO_MAXIMO_MS) { ticket.await() }
        ticketsPendientes.remove(id)
        if (respuesta == null) anotar(Direccion.SISTEMA, "Sin respuesta de la web para #$id")
        return respuesta
    }

    private fun enviarEvento(accion: String, payload: JsonObject) {
        enviar(MensajePuente(TipoMensaje.EVENT, accion, payload = payload))
    }

    private fun enviar(carta: MensajePuente) {
        if (!_state.value.conectado) {
            anotar(Direccion.SISTEMA, "No enviada, la web no está conectada: ${carta.action}")
            return
        }
        anotar(Direccion.APP_A_WEB, describir(carta))
        _cartasSalientes.trySend(ProtocoloPuente.escribir(carta))
    }

    private fun cerrarCanal() {
        if (!_state.value.conectado) return
        _state.update { it.copy(conectado = false) }
        anotar(Direccion.SISTEMA, "Canal cerrado, esperando que la web vuelva a saludar")
    }

    private fun guardarUrl() {
        val url = _state.value.urlEditable.trim()
        if (!url.startsWith("https://") || url.length <= "https://".length) {
            _state.update { it.copy(errorUrl = "Debe empezar con https://") }
            return
        }
        preferencias.guardarUrl(url)
        _state.update { it.copy(url = url, editandoUrl = false, errorUrl = null, conectado = false) }
        anotar(Direccion.SISTEMA, "URL de la web actualizada")
    }

    private fun describir(carta: MensajePuente): String {
        val cabecera = buildList {
            add(carta.type.name.lowercase())
            add(carta.action)
            carta.id?.let { add("#$it") }
            carta.ok?.let { add(if (it) "ok" else "error: ${carta.error}") }
        }.joinToString(" · ")
        if (carta.payload.isEmpty()) return cabecera
        return "$cabecera  ${carta.payload}"
    }

    private fun anotar(direccion: Direccion, detalle: String) {
        ultimaEntrada++
        val entrada = EntradaBitacora(ultimaEntrada, direccion, detalle)
        _state.update { it.copy(bitacora = (listOf(entrada) + it.bitacora).take(MAXIMO_BITACORA)) }
    }

    private companion object {
        const val TIEMPO_MAXIMO_MS = 8_000L
        const val MAXIMO_BITACORA = 100
        const val TEMA_OSCURO = "oscuro"
        const val TEMA_CLARO = "claro"
    }
}
