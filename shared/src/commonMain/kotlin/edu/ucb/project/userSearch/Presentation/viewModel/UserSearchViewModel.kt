package edu.ucb.project.userSearch.Presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import edu.ucb.project.userSearch.Domain.useCase.SearchUserUseCase

class UserSearchViewModel(
    val searchUserUseCase: SearchUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(UserSearchState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<UserSearchEffect>()
    val effect = _effect.asSharedFlow()

    fun emitEvent(event: UserSearchEvent) {
        when (event) {
            is UserSearchEvent.OnAliasChange -> {
                _state.update { it.copy(alias = event.value) }
            }
            UserSearchEvent.OnBack -> {
                emitEffect(UserSearchEffect.NavigateToBack)
            }
            UserSearchEvent.OnSubmit -> {
                viewModelScope.launch {
                    searchUserUseCase.invoke(_state.value.alias).fold(
                        onSuccess = { userInfo ->
                            _state.update { it.copy(user = userInfo, error = null) }
                        },
                        onFailure = { throwable ->
                            _state.update { it.copy(user = null, error = throwable.message) }
                        }
                    )
                }
            }
        }
    }

    private fun emitEffect(effect: UserSearchEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
