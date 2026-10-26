package edu.ucb.project.userSearch.Presentation.viewModel

sealed interface UserSearchEffect {
    data class ShowToast(val message: String) : UserSearchEffect
    object NavigateToBack : UserSearchEffect
}
