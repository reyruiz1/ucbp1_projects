package edu.ucb.project.movies.domain.repository

import edu.ucb.project.movies.domain.model.DollarModel

interface DollarRepository {
    suspend fun getList(): List<DollarModel>

    suspend fun insert(dollar: DollarModel)
}