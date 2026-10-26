package edu.ucb.project.configlocal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucb.project.configlocal.domain.repository.ConfigRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.IO
class ConfigViewModel(private val repository: ConfigRepository) : ViewModel() {
    private val _state = MutableStateFlow(ConfigState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.sembrarUnaVez()
                }
                repository.observarTodos().collect { lista ->
                    _state.update { it.copy(items = lista, cargando = false) }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update { it.copy(cargando = false, error = e.message ?: "No se pudo abrir Room.") }
            }
        }
    }

    fun emitEvent(event: ConfigEvent) {
        when (event) {
            is ConfigEvent.OnClaveChange -> _state.update { it.copy(clave = event.clave, error = null) }
            is ConfigEvent.OnValorChange -> _state.update { it.copy(valor = event.valor, error = null) }
            ConfigEvent.OnAgregar -> {
                val clave = state.value.clave.trim()
                val valor = state.value.valor.trim()
                if (clave.isEmpty() || valor.isEmpty()) {
                    _state.update { it.copy(error = "Completa clave y valor.") }
                    return
                }
                ejecutar {
                    repository.agregar(clave, valor)
                    _state.update { it.copy(clave = "", valor = "") }
                }
            }
            is ConfigEvent.OnBorrar -> ejecutar { repository.borrar(event.id) }
            ConfigEvent.OnBorrarTodos -> ejecutar { repository.borrarTodos() }
        }
    }

    private fun ejecutar(accion: suspend () -> Unit) {
        if (state.value.guardando || state.value.cargando) return
        _state.update { it.copy(guardando = true, error = null) }
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    accion()
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "No se pudo guardar el cambio.") }
            } finally {
                _state.update { it.copy(guardando = false) }
            }
        }
    }
}