package edu.ucb.project.tareas.domain.repository

import edu.ucb.project.tareas.domain.model.Tarea
import kotlinx.coroutines.flow.Flow

interface TareasRepository {
    fun observarTareas(): Flow<List<Tarea>>
    fun observarTareasNuevas(): Flow<Tarea>
    suspend fun agregar(titulo: String)
    suspend fun cambiarCompletada(tarea: Tarea)
    suspend fun eliminar(tarea: Tarea)
}
