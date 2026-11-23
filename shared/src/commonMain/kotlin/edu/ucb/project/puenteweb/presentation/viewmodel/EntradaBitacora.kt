package edu.ucb.project.puenteweb.presentation.viewmodel

enum class Direccion(val etiqueta: String) {
    WEB_A_APP("WEB → APP"),
    APP_A_WEB("APP → WEB"),
    SISTEMA("SISTEMA")
}

data class EntradaBitacora(
    val numero: Int,
    val direccion: Direccion,
    val detalle: String
)
