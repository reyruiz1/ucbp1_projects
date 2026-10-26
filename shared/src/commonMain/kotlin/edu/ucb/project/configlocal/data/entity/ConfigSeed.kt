package edu.ucb.project.configlocal.data.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "config_seed")
data class ConfigSeed(@PrimaryKey val id: Int = 1)
