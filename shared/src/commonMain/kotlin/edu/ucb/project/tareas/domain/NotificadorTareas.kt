package edu.ucb.project.tareas.domain

import edu.ucb.project.tareas.domain.model.Tarea

// commonMain no puede usar NotificationManager (es de Android), así que solo declaramos
// QUÉ se hace. La implementación real está en androidMain (NotificadorTareasAndroid).
interface NotificadorTareas {
    fun notificarNuevaTarea(tarea: Tarea)
}