package edu.ucb.project.catalogo.data.dataSource

import edu.ucb.project.catalogo.domain.model.MovieModel

interface CatalogRemoteDataSource {
    suspend fun fetchData(): Result<List<MovieModel>>
}
