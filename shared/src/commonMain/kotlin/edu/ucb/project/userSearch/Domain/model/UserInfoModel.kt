package edu.ucb.project.userSearch.Domain.model

data class UserInfoModel(
    val login: String,
    val id: Long,
    val avatarUrl: String,
    val htmlUrl: String,
    val type: String,
    val publicRepos: Int,
    val publicGists: Int,
    val followers: Int,
    val following: Int,
    val createdAt: String,
    val updatedAt: String,
    val name: String? = null,
    val company: String? = null,
    val location: String? = null,
    val email: String? = null,
    val bio: String? = null
)
