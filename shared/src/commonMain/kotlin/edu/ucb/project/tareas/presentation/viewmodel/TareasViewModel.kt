package edu.ucb.project.tareas.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucb.project.tareas.domain.PreferenciasTareas
import edu.ucb.project.tareas.domain.repository.TareasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TareasViewModel(
    private val repository: TareasRepository,
    private val preferencias: PreferenciasTareas
) : ViewModel() {

    private val _state = MutableStateFlow(TareasState(silenciado = preferencias.estaSilenciado()))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observarTareas()
                .catch { e -> mostrarError("No se pudo leer las tareas: ${e.message}") }
                .collect { lista ->
                    _state.update { it.copy(tareas = lista) }
                }
        }
    }

    fun emitEvent(event: TareasEvent) {
        when (event) {
            is TareasEvent.OnTituloChange -> {
                _state.update { it.copy(tituloNuevo = event.titulo) }
            }
            TareasEvent.OnAgregar -> {
                val titulo = _state.value.tituloNuevo.trim()
                if (titulo.isEmpty()) return
                _state.update { it.copy(tituloNuevo = "") }
                ejecutar("agregar") { repository.agregar(titulo) }
            }
            is TareasEvent.OnCambiarCompletada -> {
                ejecutar("actualizar") { repository.cambiarCompletada(event.tarea) }
            }
            is TareasEvent.OnEliminar -> {
                ejecutar("eliminar") { repository.eliminar(event.tarea) }
            }
            is TareasEvent.OnSilenciarChange -> {
                preferencias.guardarSilenciado(event.silenciado)
                _state.update { it.copy(silenciado = event.silenciado) }
            }
            TareasEvent.OnCerrarError -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    // Equivalente a OnFailureListener: si la escritura en Firestore falla, mostramos el error.
    private fun ejecutar(accion: String, bloque: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                bloque()
            } catch (e: Exception) {
                mostrarError("No se pudo $accion la tarea: ${e.message}")
            }
        }
    }

    private fun mostrarError(mensaje: String) {
        println("TareasViewModel ERROR: $mensaje")
        _state.update { it.copy(error = mensaje) }
    }
}
