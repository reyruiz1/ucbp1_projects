package edu.ucb.project.userSearch.Presentation.viewModel

import edu.ucb.project.userSearch.Domain.model.UserInfoModel

data class UserSearchState(
    val alias: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: UserInfoModel? = null
)
