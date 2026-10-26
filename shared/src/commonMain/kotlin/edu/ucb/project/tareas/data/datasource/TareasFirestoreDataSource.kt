package edu.ucb.project.tareas.data.datasource

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.QuerySnapshot
import dev.gitlive.firebase.firestore.firestore
import edu.ucb.project.tareas.data.dto.TareaDto
import kotlinx.coroutines.flow.Flow

class TareasFirestoreDataSource {

    private val coleccion by lazy { Firebase.firestore.collection("tareas") }

    fun observarCambios(): Flow<QuerySnapshot> {
        return coleccion.snapshots
    }

    suspend fun agregar(tarea: TareaDto) {
        coleccion.add(tarea)
    }

    suspend fun actualizar(id: String, tarea: TareaDto) {
        coleccion.document(id).set(tarea)
    }

    suspend fun eliminar(id: String) {
        coleccion.document(id).delete()
    }
}
