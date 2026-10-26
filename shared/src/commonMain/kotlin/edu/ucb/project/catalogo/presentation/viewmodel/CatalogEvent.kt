package edu.ucb.project.catalogo.presentation.viewmodel

import edu.ucb.project.catalogo.domain.model.MovieModel

sealed interface CatalogEvent {
    object OnLoad : CatalogEvent
    data class OnShowDetail(val movie: MovieModel) : CatalogEvent
}
