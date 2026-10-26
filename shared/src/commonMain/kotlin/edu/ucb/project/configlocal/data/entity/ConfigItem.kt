package edu.ucb.project.configlocal.data.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "config_items")
data class ConfigItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clave: String,
    val valor: String,
    val timestamp: Long
)
