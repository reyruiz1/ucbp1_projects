package edu.ucb.project.tareas

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import edu.ucb.project.navigation.Destinos
import edu.ucb.project.notificaciones.CanalesNotificacion
import edu.ucb.project.tareas.domain.NotificadorTareas
import edu.ucb.project.tareas.domain.model.Tarea

class NotificadorTareasAndroid(
    private val context: Context
) : NotificadorTareas {

    override fun notificarNuevaTarea(tarea: Tarea) {
        if (!tienePermiso()) {
            println("NotificadorTareas: sin permiso POST_NOTIFICATIONS, no se muestra")
            return
        }

        val notificacion = NotificationCompat.Builder(context, CanalesNotificacion.CANAL_TAREAS)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Nueva tarea: ${tarea.titulo}")
            .setContentText("Toca para ver la lista de tareas")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(crearPendingIntent(tarea))
            .setAutoCancel(true)
            .build()

        // Usamos el id del documento como id de notificación: cada tarea nueva es una notificación aparte.
        NotificationManagerCompat.from(context).notify(tarea.id.hashCode(), notificacion)
    }

    // PendingIntent = un Intent "guardado" que Android ejecuta cuando el usuario toca la notificación.
    private fun crearPendingIntent(tarea: Tarea): PendingIntent? {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            ?: return null
        intent.putExtra(Destinos.EXTRA_DESTINO, Destinos.TAREAS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        return PendingIntent.getActivity(
            context,
            tarea.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun tienePermiso(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }
}