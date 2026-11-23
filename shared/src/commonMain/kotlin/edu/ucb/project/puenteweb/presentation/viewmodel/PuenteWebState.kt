package edu.ucb.project.puenteweb.presentation.viewmodel

data class PuenteWebState(
    val url: String = "",
    val recargas: Int = 0,
    val conectado: Boolean = false,
    val modoOscuro: Boolean = false,
    val bitacora: List<EntradaBitacora> = emptyList(),
    val bitacoraVisible: Boolean = false,
    val editandoUrl: Boolean = false,
    val urlEditable: String = "",
    val errorUrl: String? = null,
    val aviso: String? = null
)
