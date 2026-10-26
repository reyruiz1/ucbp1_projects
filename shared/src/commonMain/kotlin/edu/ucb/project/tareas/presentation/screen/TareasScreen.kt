package edu.ucb.project.tareas.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import edu.ucb.project.tareas.domain.model.Tarea
import edu.ucb.project.tareas.presentation.viewmodel.TareasEvent
import edu.ucb.project.tareas.presentation.viewmodel.TareasViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TareasScreen(viewModel: TareasViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Tareas", style = MaterialTheme.typography.headlineMedium)

        // Formulario de alta
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = state.tituloNuevo,
                onValueChange = { viewModel.emitEvent(TareasEvent.OnTituloChange(it)) },
                label = { Text("Nueva tarea") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { viewModel.emitEvent(TareasEvent.OnAgregar) },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Agregar")
            }
        }

        // Silenciar notificaciones (se guarda en SharedPreferences)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Silenciar notificaciones", modifier = Modifier.weight(1f))
            Switch(
                checked = state.silenciado,
                onCheckedChange = { viewModel.emitEvent(TareasEvent.OnSilenciarChange(it)) }
            )
        }

        state.error?.let { mensaje ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    mensaje,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = { viewModel.emitEvent(TareasEvent.OnCerrarError) }) {
                    Text("OK")
                }
            }
        }

        HorizontalDivider()

        if (state.tareas.isEmpty()) {
            Text("No hay tareas todavía")
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(state.tareas, key = { it.id }) { tarea ->
                TareaItem(
                    tarea = tarea,
                    onCambiarCompletada = { viewModel.emitEvent(TareasEvent.OnCambiarCompletada(tarea)) },
                    onEliminar = { viewModel.emitEvent(TareasEvent.OnEliminar(tarea)) }
                )
            }
        }
    }
}

@Composable
private fun TareaItem(
    tarea: Tarea,
    onCambiarCompletada: () -> Unit,
    onEliminar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = tarea.completada,
            onCheckedChange = { onCambiarCompletada() }
        )
        Text(
            text = tarea.titulo,
            textDecoration = if (tarea.completada) TextDecoration.LineThrough else null,
            modifier = Modifier.weight(1f)
        )
        TextButton(onClick = onEliminar) {
            Text("Eliminar")
        }
    }
}
