package edu.ucb.project.configlocal.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.ucb.project.configlocal.presentation.viewmodel.ConfigEvent
import edu.ucb.project.configlocal.presentation.viewmodel.ConfigViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Composable
fun ConfigLocalScreen(viewModel: ConfigViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val disponible = !state.cargando && !state.guardando

    // 👇 Envolvemos el LazyColumn en un Surface para darle el fondo del tema
    androidx.compose.material3.Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().safeDrawingPadding().imePadding().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Configuración local", style = MaterialTheme.typography.headlineSmall)
                Text("E25-2 · Mateo Cossio")
            }
            item {
                OutlinedTextField(
                    value = state.clave,
                    onValueChange = { viewModel.emitEvent(ConfigEvent.OnClaveChange(it)) },
                    label = { Text("Clave") },
                    singleLine = true,
                    enabled = disponible,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = state.valor,
                    onValueChange = { viewModel.emitEvent(ConfigEvent.OnValorChange(it)) },
                    label = { Text("Valor") },
                    singleLine = true,
                    enabled = disponible,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        enabled = disponible,
                        onClick = { viewModel.emitEvent(ConfigEvent.OnAgregar) }
                    ) { Text("Agregar") }
                    OutlinedButton(
                        enabled = disponible && state.items.isNotEmpty(),
                        onClick = { viewModel.emitEvent(ConfigEvent.OnBorrarTodos) }
                    ) { Text("Borrar todo") }
                }
            }
            item {
                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                Text(if (state.cargando) "Cargando..." else "Registros: ${state.items.size}")
                if (!state.cargando && state.items.isEmpty()) Text("Sin configuraciones")
                HorizontalDivider()
            }
            items(state.items, key = { it.id }) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("${item.clave} = ${item.valor}")
                        Text(
                            "ID: ${item.id} · ${Instant.fromEpochMilliseconds(item.timestamp)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    TextButton(
                        enabled = disponible,
                        onClick = { viewModel.emitEvent(ConfigEvent.OnBorrar(item.id)) }
                    ) { Text("Borrar") }
                }
                HorizontalDivider()
            }
        }
    }
}