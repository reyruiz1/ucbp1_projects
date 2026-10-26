package edu.ucb.project.movies.data.repository

import edu.ucb.project.movies.data.datasource.DollarLocalDataSource
import edu.ucb.project.movies.domain.model.DollarModel
import edu.ucb.project.movies.domain.repository.DollarRepository

class DollarRepositoryImpl(
    val localDataSource: DollarLocalDataSource
): DollarRepository {
    override suspend fun getList(): List<DollarModel> {
        return localDataSource.getList()
    }

    override suspend fun insert(dollar: DollarModel) {
        localDataSource.insert(dollar)
    }
}