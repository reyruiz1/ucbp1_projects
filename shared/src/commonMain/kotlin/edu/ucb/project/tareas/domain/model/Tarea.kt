package edu.ucb.project.tareas.domain.model

data class Tarea(
    val id: String,
    val titulo: String,
    val completada: Boolean,
    val timestamp: Long
)
