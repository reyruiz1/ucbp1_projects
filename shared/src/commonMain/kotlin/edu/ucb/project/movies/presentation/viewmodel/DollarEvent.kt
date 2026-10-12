package edu.ucb.project.movies.presentation.viewmodel

sealed interface DollarEvent {
    object OnAddRecord: DollarEvent
}