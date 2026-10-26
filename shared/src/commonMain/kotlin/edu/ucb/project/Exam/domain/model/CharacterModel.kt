package edu.ucb.project.Exam.domain.model

import firstkotlinproject.shared.generated.resources.Res

data class CharacterModel (
    val id : String,
    val name : String,
    val height : String,
    val mass : String,
    val hairColor : String,
    val skinColor : String,
    val eyeColor : String,
    val gender : String
)