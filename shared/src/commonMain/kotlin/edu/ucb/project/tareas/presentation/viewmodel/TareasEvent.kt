package edu.ucb.project.tareas.presentation.viewmodel

import edu.ucb.project.tareas.domain.model.Tarea

sealed class TareasEvent {
    data class OnTituloChange(val titulo: String) : TareasEvent()
    object OnAgregar : TareasEvent()
    data class OnCambiarCompletada(val tarea: Tarea) : TareasEvent()
    data class OnEliminar(val tarea: Tarea) : TareasEvent()
    data class OnSilenciarChange(val silenciado: Boolean) : TareasEvent()
    object OnCerrarError : TareasEvent()
}
