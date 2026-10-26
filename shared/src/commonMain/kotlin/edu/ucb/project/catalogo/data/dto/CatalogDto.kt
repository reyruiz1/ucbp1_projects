package edu.ucb.project.catalogo.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CatalogDto(
    val page: Int? = null,
    @SerialName("results")
    val result: List<MovieDto> = emptyList(),
)
