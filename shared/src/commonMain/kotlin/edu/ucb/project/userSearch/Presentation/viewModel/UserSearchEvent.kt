package edu.ucb.project.userSearch.Presentation.viewModel

sealed interface UserSearchEvent {
    object OnBack : UserSearchEvent
    object OnSubmit : UserSearchEvent
    data class OnAliasChange(val value: String) : UserSearchEvent
}
