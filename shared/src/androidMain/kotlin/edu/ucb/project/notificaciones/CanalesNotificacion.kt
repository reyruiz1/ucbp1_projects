package edu.ucb.project.notificaciones

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object CanalesNotificacion {
    const val CANAL_GENERAL = "canal_general"
    const val CANAL_TAREAS = "canal_tareas"

    fun crear(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val general = NotificationChannel(
            CANAL_GENERAL,
            "General",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notificaciones generales de la app"
        }

        val tareas = NotificationChannel(
            CANAL_TAREAS,
            "Tareas",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Aviso cuando se agrega una tarea nueva en Firestore"
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannels(listOf(general, tareas))
    }
}
