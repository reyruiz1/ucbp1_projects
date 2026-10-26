package edu.ucb.project.catalogo.domain.usecase

import edu.ucb.project.catalogo.domain.model.MovieModel
import edu.ucb.project.catalogo.domain.repository.CatalogRespository

class GetMoviesUsecase(val repository : CatalogRespository) {
    suspend fun invoke () : Result<List<MovieModel>>{
        return repository.getMovie()
    }
}