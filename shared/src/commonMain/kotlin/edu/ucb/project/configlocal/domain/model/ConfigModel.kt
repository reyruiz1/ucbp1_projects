package edu.ucb.project.configlocal.domain.model

data class ConfigModel(
    val id: Long,
    val clave: String,
    val valor: String,
    val timestamp: Long
)
