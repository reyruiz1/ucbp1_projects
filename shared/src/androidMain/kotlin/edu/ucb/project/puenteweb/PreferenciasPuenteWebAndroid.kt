package edu.ucb.project.puenteweb

import android.content.Context
import androidx.core.content.edit
import edu.ucb.project.puenteweb.domain.PreferenciasPuenteWeb

class PreferenciasPuenteWebAndroid(
    context: Context
) : PreferenciasPuenteWeb {

    private val prefs = context.getSharedPreferences("puente_web_prefs", Context.MODE_PRIVATE)

    override fun obtenerUrl(): String {
        return prefs.getString(CLAVE_URL, "").orEmpty()
    }

    override fun guardarUrl(url: String) {
        prefs.edit { putString(CLAVE_URL, url) }
    }

    override fun obtenerModoOscuro(): Boolean {
        return prefs.getBoolean(CLAVE_MODO_OSCURO, false)
    }

    override fun guardarModoOscuro(activo: Boolean) {
        prefs.edit { putBoolean(CLAVE_MODO_OSCURO, activo) }
    }

    private companion object {
        const val CLAVE_URL = "url_web"
        const val CLAVE_MODO_OSCURO = "modo_oscuro"
    }
}
