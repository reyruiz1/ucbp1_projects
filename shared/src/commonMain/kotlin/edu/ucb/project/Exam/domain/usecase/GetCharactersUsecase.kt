package edu.ucb.project.Exam.domain.usecase

import edu.ucb.project.Exam.domain.model.CharacterModel
import edu.ucb.project.Exam.domain.repository.PeopleRespository

class GetCharactersUsecase(val repository : PeopleRespository) {
    suspend fun invoke (page: Int) : Result<List<CharacterModel>>{
        return repository.getCharacter(page)
    }
}