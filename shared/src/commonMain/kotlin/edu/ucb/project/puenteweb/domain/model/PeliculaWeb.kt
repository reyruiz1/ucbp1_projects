package edu.ucb.project.puenteweb.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PeliculaWeb(
    val titulo: String,
    val poster: String
)
