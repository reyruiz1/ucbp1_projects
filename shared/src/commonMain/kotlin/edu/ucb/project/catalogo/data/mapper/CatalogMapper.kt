package edu.ucb.project.catalogo.data.mapper

import edu.ucb.project.catalogo.data.dto.MovieDto
import edu.ucb.project.catalogo.domain.model.MovieModel

fun MovieDto.toDomain(): MovieModel = MovieModel(
    id = "",
    title = title ?: "",
    posterPath = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""
)
