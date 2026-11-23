package edu.ucb.project.puenteweb.domain

interface PreferenciasPuenteWeb {
    fun obtenerUrl(): String
    fun guardarUrl(url: String)
    fun obtenerModoOscuro(): Boolean
    fun guardarModoOscuro(activo: Boolean)
}
