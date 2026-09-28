package edu.ucb.project.catalogo.presentation.viewmodel

import edu.ucb.project.catalogo.domain.model.MovieModel

sealed interface CatalogEffect {
    data class ShowToast(val message: String) : CatalogEffect
    data class NavigateToDetail(val movie: MovieModel) : CatalogEffect
}
