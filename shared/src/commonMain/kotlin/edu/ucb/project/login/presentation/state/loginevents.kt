package edu.ucb.project.login.presentation.state

sealed interface LoginEvent {
    data class EmailChanged(val value: String) : LoginEvent
    data class PasswordChanged(val value: String) : LoginEvent
    data object LoginPressed : LoginEvent
}