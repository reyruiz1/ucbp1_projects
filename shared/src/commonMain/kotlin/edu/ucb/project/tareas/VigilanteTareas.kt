package edu.ucb.project.tareas

import edu.ucb.project.tareas.domain.NotificadorTareas
import edu.ucb.project.tareas.domain.repository.TareasRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform

// Escucha Firestore durante toda la vida de la app (no solo cuando la pantalla de tareas
// está abierta) y lanza una notificación por cada tarea nueva.
class VigilanteTareas(
    private val repository: TareasRepository,
    private val notificador: NotificadorTareas,
    private val preferencias: PreferenciasTareas
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var job: Job? = null

    fun iniciar() {
        if (job != null) return
        job = scope.launch {
            repository.observarTareasNuevas()
                .catch { e -> println("VigilanteTareas ERROR: ${e.message}") }
                .collect { tarea ->
                    if (!preferencias.estaSilenciado()) {
                        notificador.notificarNuevaTarea(tarea)
                    }
                }
        }
    }
}

// Llamar una sola vez, después de iniciar Koin (desde MainApplication).
fun iniciarVigilanteTareas() {
    KoinPlatform.getKoin().get<VigilanteTareas>().iniciar()
}