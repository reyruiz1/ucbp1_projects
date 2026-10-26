package edu.ucb.project.catalogo.presentation.viewmodel

import edu.ucb.project.catalogo.domain.model.MovieModel

data class CatalogState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val movies: List<MovieModel> = emptyList()
)
