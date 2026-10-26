package edu.ucb.project.Exam.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucb.project.Exam.domain.usecase.GetCharactersUsecase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PeopleViewModel(val usecase: GetCharactersUsecase) : ViewModel() {
    private val _effect = MutableSharedFlow<PeopleEffect>()
    val effect = _effect.asSharedFlow()
    private val _state = MutableStateFlow(PeopleState())
    val state = _state.asStateFlow()

    fun emitEvent(event: PeopleEvent) {
        when (event) {
            PeopleEvent.OnLoad -> {
                if (_state.value.characters.isEmpty()) {
                    loadPage(1)
                }
            }
            PeopleEvent.OnLoadNextPage -> {
                // evita pedir dos veces la misma pagina o pedir despues de la ultima
                if (!_state.value.isLoading && !_state.value.isLastPage) {
                    loadPage(_state.value.page + 1)
                }
            }
            is PeopleEvent.OnShowDetail -> {
                emitEffect(PeopleEffect.NavigateToDetail(event.character))
            }
        }
    }

    private fun loadPage(page: Int) {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            // runCatching: PeopleService relanza la excepcion si no hay red
            runCatching { usecase.invoke(page).getOrThrow() }.fold(
                onSuccess = { characters ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            characters = it.characters + characters,
                            page = page,
                            // SWAPI manda 10 personajes por pagina, si llegan menos es la ultima
                            isLastPage = characters.size < 10,
                            error = null
                        )
                    }
                },
                onFailure = { throwable ->
                    _state.update { it.copy(isLoading = false, error = throwable.message) }
                }
            )
        }
    }

    private fun emitEffect(effect: PeopleEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
