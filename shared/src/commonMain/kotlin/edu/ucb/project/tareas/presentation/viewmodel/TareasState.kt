package edu.ucb.project.tareas.presentation.viewmodel

import edu.ucb.project.tareas.domain.model.Tarea

data class TareasState(
    val tareas: List<Tarea> = emptyList(),
    val tituloNuevo: String = "",
    val silenciado: Boolean = false,
    val error: String? = null
)
