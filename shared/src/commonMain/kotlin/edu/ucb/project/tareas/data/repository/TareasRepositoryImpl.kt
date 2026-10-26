package edu.ucb.project.tareas.data.repository

import dev.gitlive.firebase.firestore.ChangeType
import dev.gitlive.firebase.firestore.DocumentSnapshot
import edu.ucb.project.tareas.data.datasource.TareasFirestoreDataSource
import edu.ucb.project.tareas.data.dto.TareaDto
import edu.ucb.project.tareas.domain.model.Tarea
import edu.ucb.project.tareas.domain.repository.TareasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class TareasRepositoryImpl(
    private val dataSource: TareasFirestoreDataSource
) : TareasRepository {

    override fun observarTareas(): Flow<List<Tarea>> {
        return dataSource.observarCambios().map { snapshot ->
            snapshot.documents
                .mapNotNull { it.toTarea() }
                .sortedByDescending { it.timestamp }
        }
    }

    override fun observarTareasNuevas(): Flow<Tarea> = flow {
        var esPrimeraCarga = true
        dataSource.observarCambios().collect { snapshot ->
            if (esPrimeraCarga) {
                esPrimeraCarga = false
                return@collect
            }
            snapshot.documentChanges
                .filter { it.type == ChangeType.ADDED }
                .mapNotNull { it.document.toTarea() }
                .forEach { emit(it) }
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun agregar(titulo: String) {
        val dto = TareaDto(
            titulo = titulo,
            completada = false,
            timestamp = Clock.System.now().toEpochMilliseconds()
        )
        dataSource.agregar(dto)
    }

    override suspend fun cambiarCompletada(tarea: Tarea) {
        val dto = TareaDto(
            titulo = tarea.titulo,
            completada = !tarea.completada,
            timestamp = tarea.timestamp
        )
        dataSource.actualizar(tarea.id, dto)
    }

    override suspend fun eliminar(tarea: Tarea) {
        dataSource.eliminar(tarea.id)
    }

    private fun DocumentSnapshot.toTarea(): Tarea? {
        return try {
            val dto = data<TareaDto>()
            Tarea(
                id = id,
                titulo = dto.titulo,
                completada = dto.completada,
                timestamp = dto.timestamp
            )
        } catch (e: Exception) {
            println("TareasRepository: documento $id inválido -> ${e.message}")
            null
        }
    }
}
