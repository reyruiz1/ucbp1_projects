package edu.ucb.project.movies.presentation.viewmodel

import edu.ucb.project.movies.domain.model.DollarModel

data class DollarState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val list: List<DollarModel> = emptyList()
)