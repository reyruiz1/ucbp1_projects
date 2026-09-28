package edu.ucb.project.Exam.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PeopleDto(
    val count: Int? = null,
    @SerialName("results")
    val result: List<CharacterDto> = emptyList(),
)
