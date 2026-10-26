
package edu.ucb.project.notificaciones

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object CanalNotificacionesE25 {

    const val ID = "gabriel_e25_5"

    fun crear(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                ID,
                "Notificaciones locales E25-5",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para las notificaciones de Gabriel"
            }

            val manager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            manager.createNotificationChannel(canal)
        }
    }
}
