package edu.ucb.project.puenteweb.presentation.viewmodel

sealed class PuenteWebEvent {
    data class OnCartaRecibida(val texto: String) : PuenteWebEvent()
    object OnCanalCerrado : PuenteWebEvent()
    data class OnModoOscuroChange(val activo: Boolean) : PuenteWebEvent()
    object OnPedirSeleccion : PuenteWebEvent()
    object OnRecargar : PuenteWebEvent()
    object OnAbrirUrl : PuenteWebEvent()
    data class OnUrlChange(val url: String) : PuenteWebEvent()
    object OnGuardarUrl : PuenteWebEvent()
    object OnCerrarUrl : PuenteWebEvent()
    object OnCerrarAviso : PuenteWebEvent()
    object OnLimpiarBitacora : PuenteWebEvent()
    object OnAlternarBitacora : PuenteWebEvent()
}
