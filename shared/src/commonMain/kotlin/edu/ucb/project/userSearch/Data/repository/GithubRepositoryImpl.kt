package edu.ucb.project.userSearch.Data.repository

import edu.ucb.project.userSearch.Domain.model.UserInfoModel
import edu.ucb.project.userSearch.Domain.repository.GithubRepository

class GithubRepositoryImpl : GithubRepository {
    override suspend fun findByUserId(userId: String): Result<UserInfoModel> {
        // Perfil hardcodeado con los datos reales de https://api.github.com/users/reyruiz1
        // (sin llamada HTTP real todavia). Siempre devuelve este perfil.
        return Result.success(
            UserInfoModel(
                login = "reyruiz1",
                id = 143035315L,
                avatarUrl = "https://avatars.githubusercontent.com/u/143035315?v=4",
                htmlUrl = "https://github.com/reyruiz1",
                type = "User",
                publicRepos = 26,
                publicGists = 0,
                followers = 0,
                following = 0,
                createdAt = "2023-08-23T20:33:58Z",
                updatedAt = "2026-08-17T22:46:46Z",
                name = null,
                company = null,
                location = null,
                email = null,
                bio = null
            )
        )
    }
}
