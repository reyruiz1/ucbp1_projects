
package edu.ucb.project.notificaciones

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import edu.ucb.project.notificaciones.domain.NotificadorLocal
import edu.ucb.project.shared.R

class NotificadorLocalAndroid(
    private val context: Context
) : NotificadorLocal {

    private var siguienteId = 2505

    override fun permisoConcedido(): Boolean {
        val permisoAndroid13 =
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED

        val notificacionesActivas =
            NotificationManagerCompat.from(context)
                .areNotificationsEnabled()

        val canalActivo =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val manager = context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as NotificationManager

                manager.getNotificationChannel(
                    CanalNotificacionesE25.ID
                )?.importance != NotificationManager.IMPORTANCE_NONE
            } else {
                true
            }

        return permisoAndroid13 &&
                notificacionesActivas &&
                canalActivo
    }

    @SuppressLint("MissingPermission")
    override fun mostrarSaludo(): Boolean {
        CanalNotificacionesE25.crear(context)

        if (!permisoConcedido()) return false

        val intent = context.packageManager
            .getLaunchIntentForPackage(context.packageName)
            ?: return false

        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        )

        val pendingIntent = PendingIntent.getActivity(
            context,
            2505,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val mensaje =
            "Esta notificación fue creada localmente " +
                    "con NotificationManager. Ejercicio E25-5."

        val notificacion = NotificationCompat.Builder(
            context,
            CanalNotificacionesE25.ID
        )
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle("Hola desde la app")
            .setContentText(mensaje)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(mensaje)
            )
            .setColor(0xFF6200EE.toInt())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        return try {
            NotificationManagerCompat.from(context)
                .notify(siguienteId++, notificacion)
            true
        } catch (e: SecurityException) {
            false
        }
    }

    override fun abrirAjustesDeNotificaciones() {
        val intent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .putExtra(
                        Settings.EXTRA_APP_PACKAGE,
                        context.packageName
                    )
            } else {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(
                        Uri.parse("package:${context.packageName}")
                    )
            }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
