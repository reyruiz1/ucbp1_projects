package edu.ucb.project.Exam.presentation.viewmodel

import edu.ucb.project.Exam.domain.model.CharacterModel

sealed interface PeopleEffect {
    data class ShowToast(val message: String) : PeopleEffect
    data class NavigateToDetail(val character: CharacterModel) : PeopleEffect
}
