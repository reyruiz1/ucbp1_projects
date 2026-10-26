package edu.ucb.project.catalogo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucb.project.catalogo.domain.usecase.GetMoviesUsecase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogViewModel(val usecase: GetMoviesUsecase) : ViewModel() {
    private val _effect = MutableSharedFlow<CatalogEffect>()
    val effect = _effect.asSharedFlow()
    private val _state = MutableStateFlow(CatalogState())
    val state = _state.asStateFlow()

    fun emitEvent(event: CatalogEvent) {
        when (event) {
            CatalogEvent.OnLoad -> {
                _state.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    // runCatching: CatalogService relanza la excepcion si no hay red
                    runCatching { usecase.invoke().getOrThrow() }.fold(
                        onSuccess = { movies ->
                            _state.update { it.copy(isLoading = false, movies = movies, error = null) }
                        },
                        onFailure = { throwable ->
                            _state.update { it.copy(isLoading = false, error = throwable.message) }
                        }
                    )
                }
            }
            is CatalogEvent.OnShowDetail -> {
                emitEffect(CatalogEffect.NavigateToDetail(event.movie))
            }
        }
    }

    private fun emitEffect(effect: CatalogEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
