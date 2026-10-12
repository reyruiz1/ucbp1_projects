package edu.ucb.project.movies.presentation.viewmodel

interface DollarEffect {
    data class ShowToast(val message: String): DollarEffect
}