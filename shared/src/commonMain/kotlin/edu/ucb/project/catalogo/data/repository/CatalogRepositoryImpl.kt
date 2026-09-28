package edu.ucb.project.catalogo.data.repository

import edu.ucb.project.catalogo.data.dataSource.CatalogRemoteDataSource
import edu.ucb.project.catalogo.domain.model.MovieModel
import edu.ucb.project.catalogo.domain.repository.CatalogRespository

class CatalogRepositoryImpl(val dataSource: CatalogRemoteDataSource) : CatalogRespository {
    override suspend fun getMovie(): Result<List<MovieModel>> {
        return dataSource.fetchData()
    }
}
