package edu.ucb.project.tareas.domain

interface PreferenciasTareas {
    fun estaSilenciado(): Boolean
    fun guardarSilenciado(silenciado: Boolean)
}