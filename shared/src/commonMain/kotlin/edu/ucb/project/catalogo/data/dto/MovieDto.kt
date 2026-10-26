package edu.ucb.project.catalogo.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    val title: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
)
