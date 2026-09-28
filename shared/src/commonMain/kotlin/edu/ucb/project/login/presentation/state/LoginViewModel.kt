package edu.ucb.project.login.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import edu.ucb.project.login.domain.usecase.LoginUseCase
import edu.ucb.project.login.domain.vo.EmailVo
import edu.ucb.project.login.domain.vo.PasswordVo

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<LoginEffects>()
    val effects = _effects.asSharedFlow()

    private fun emitEffect(effect: LoginEffects) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }

    fun emitEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.update { it.copy(email = event.value) }
            }
            is LoginEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.value) }
            }
            LoginEvent.LoginPressed -> {
                var isValid = true
                if (state.value.email.isBlank()) {
                    emitEffect(LoginEffects.ShowToast("The email field is required"))
                    isValid = false
                }
                if (state.value.password.isBlank()) {
                    emitEffect(LoginEffects.ShowToast("The password field is required"))
                    isValid = false
                }

                if (isValid) {
                    viewModelScope.launch {
                        loginUseCase(EmailVo(state.value.email), PasswordVo(state.value.password))
                            .fold(
                                onSuccess = {
                                    emitEffect(LoginEffects.NavigateToHome)
                                },
                                onFailure = {
                                    emitEffect(LoginEffects.ShowToast("Credential Invalid: ${it.message}"))
                                }
                            )
                    }
                }
            }
        }
    }
}
