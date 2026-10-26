package edu.ucb.project.tareas

import android.content.Context
import androidx.core.content.edit
import edu.ucb.project.tareas.domain.PreferenciasTareas

class PreferenciasTareasAndroid(
    context: Context
) : PreferenciasTareas {

    private val prefs = context.getSharedPreferences("tareas_prefs", Context.MODE_PRIVATE)

    override fun estaSilenciado(): Boolean {
        return prefs.getBoolean(CLAVE_SILENCIADO, false)
    }

    override fun guardarSilenciado(silenciado: Boolean) {
        prefs.edit { putBoolean(CLAVE_SILENCIADO, silenciado) }
    }

    private companion object {
        const val CLAVE_SILENCIADO = "notificaciones_silenciadas"
    }
}
