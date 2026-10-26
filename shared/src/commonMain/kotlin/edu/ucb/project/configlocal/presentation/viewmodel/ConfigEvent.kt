package edu.ucb.project.configlocal.presentation.viewmodel

sealed class ConfigEvent {
    data class OnClaveChange(val clave: String) : ConfigEvent()
    data class OnValorChange(val valor: String) : ConfigEvent()
    object OnAgregar : ConfigEvent()
    data class OnBorrar(val id: Long) : ConfigEvent()
    object OnBorrarTodos : ConfigEvent()
}
