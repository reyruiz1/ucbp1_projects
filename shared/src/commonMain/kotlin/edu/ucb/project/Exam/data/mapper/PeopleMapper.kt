package edu.ucb.project.Exam.data.mapper

import edu.ucb.project.Exam.data.dto.CharacterDto
import edu.ucb.project.Exam.domain.model.CharacterModel

fun CharacterDto.toDomain(): CharacterModel = CharacterModel(
    id = "",
    name = name ?: "",
    height = height ?: "",
    mass = mass ?: "",
    hairColor = hairColor ?: "",
    skinColor = skinColor ?: "",
    eyeColor = eyeColor ?: "",
    gender = gender ?: ""
)
