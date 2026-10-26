package edu.ucb.project.tareas.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TareaDto(
    val titulo: String = "",
    val completada: Boolean = false,
    val timestamp: Long = 0L
)
