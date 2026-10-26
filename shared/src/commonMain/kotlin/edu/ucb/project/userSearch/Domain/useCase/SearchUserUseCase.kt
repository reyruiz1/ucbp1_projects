package edu.ucb.project.userSearch.Domain.useCase

import edu.ucb.project.userSearch.Domain.model.UserInfoModel
import edu.ucb.project.userSearch.Domain.repository.GithubRepository

class SearchUserUseCase(val repository: GithubRepository) {
    suspend fun invoke(userId: String): Result<UserInfoModel> {
        return repository.findByUserId(userId)
    }
}
