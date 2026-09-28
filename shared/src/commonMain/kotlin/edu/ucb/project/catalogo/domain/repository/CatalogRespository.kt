package edu.ucb.project.catalogo.domain.repository

import edu.ucb.project.catalogo.domain.model.MovieModel

interface CatalogRespository {
    suspend fun getMovie(): Result<List<MovieModel>>
}
