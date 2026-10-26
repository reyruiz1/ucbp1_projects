package edu.ucb.project.Exam.domain.repository

import edu.ucb.project.Exam.domain.model.CharacterModel

interface PeopleRespository {
    suspend fun getCharacter(page: Int): Result<List<CharacterModel>>
}
