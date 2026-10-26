package edu.ucb.project.configlocal.presentation.viewmodel

import edu.ucb.project.configlocal.domain.model.ConfigModel

data class ConfigState(
    val items: List<ConfigModel> = emptyList(),
    val clave: String = "",
    val valor: String = "",
    val cargando: Boolean = true,
    val guardando: Boolean = false,
    val error: String? = null
)
