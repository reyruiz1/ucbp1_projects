package edu.ucb.project.login.presentation.state

sealed interface LoginEffects {
    data class ShowToast(val message: String) : LoginEffects
    data object NavigateToHome : LoginEffects
}
