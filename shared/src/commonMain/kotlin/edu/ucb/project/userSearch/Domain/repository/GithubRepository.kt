package edu.ucb.project.userSearch.Domain.repository

import edu.ucb.project.userSearch.Domain.model.UserInfoModel

interface GithubRepository {
    suspend fun findByUserId(userId: String): Result<UserInfoModel>
}
