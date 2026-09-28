package edu.ucb.project.Exam.data.dataSource

import edu.ucb.project.Exam.domain.model.CharacterModel

interface PeopleRemoteDataSource {
    suspend fun fetchData(page: Int): Result<List<CharacterModel>>
}
