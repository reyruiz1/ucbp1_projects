package edu.ucb.project.puenteweb.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TareaWeb(
    val id: String,
    val titulo: String,
    val completada: Boolean
)
