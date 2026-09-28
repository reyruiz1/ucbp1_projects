package edu.ucb.project.Exam.presentation.viewmodel

import edu.ucb.project.Exam.domain.model.CharacterModel

sealed interface PeopleEvent {
    object OnLoad : PeopleEvent
    object OnLoadNextPage : PeopleEvent
    data class OnShowDetail(val character: CharacterModel) : PeopleEvent
}
