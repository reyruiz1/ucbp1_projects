package edu.ucb.project.Exam.presentation.viewmodel

import edu.ucb.project.Exam.domain.model.CharacterModel

data class PeopleState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val characters: List<CharacterModel> = emptyList(),
    val page: Int = 0,
    val isLastPage: Boolean = false
)
