package edu.ucb.project.Exam.data.repository

import edu.ucb.project.Exam.data.dataSource.PeopleRemoteDataSource
import edu.ucb.project.Exam.domain.model.CharacterModel
import edu.ucb.project.Exam.domain.repository.PeopleRespository

class PeopleRepositoryImpl(val dataSource: PeopleRemoteDataSource) : PeopleRespository {
    override suspend fun getCharacter(page: Int): Result<List<CharacterModel>> {
        return dataSource.fetchData(page)
    }
}
