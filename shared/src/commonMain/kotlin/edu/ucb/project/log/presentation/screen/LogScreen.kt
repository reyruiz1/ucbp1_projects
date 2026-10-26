package edu.ucb.project.log.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.ucb.project.log.domain.model.LogLevel
import edu.ucb.project.log.presentation.viewmodel.LogEvent
import edu.ucb.project.log.presentation.viewmodel.LogState
import edu.ucb.project.log.presentation.viewmodel.LogViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LogScreen(
    navController: NavHostController,
    viewModel: LogViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState()

    LogContent(
        state = state.value,
        onServerUrlChange = { viewModel.emitEvent(LogEvent.ServerUrlChanged(it)) },
        onMessageChange = { viewModel.emitEvent(LogEvent.MessageChanged(it)) },
        onLevelSelected = { viewModel.emitEvent(LogEvent.LevelSelected(it)) },
        onSend = { viewModel.emitEvent(LogEvent.SendPressed) },
        onBack = { navController.popBackStack() }
    )
}

@Composable
private fun LogContent(
    state: LogState,
    onServerUrlChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onLevelSelected: (LogLevel) -> Unit,
    onSend: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enviar logs por gRPC", style = MaterialTheme.typography.titleLarge)

        TextField(
            value = state.serverUrl,
            onValueChange = onServerUrlChange,
            label = { Text("URL del servidor (ngrok o http://10.0.2.2:9090)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = state.message,
            onValueChange = onMessageChange,
            label = { Text("Mensaje") },
            modifier = Modifier.fillMaxWidth()
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LogLevel.entries.forEach { level ->
                FilterChip(
                    selected = state.level == level,
                    onClick = { onLevelSelected(level) },
                    label = { Text(level.name) }
                )
            }
        }

        Button(onClick = onSend, enabled = !state.isLoading) {
            Text("Enviar log")
        }

        when {
            state.isLoading -> CircularProgressIndicator()
            state.result != null -> Text("Respuesta: ${state.result}", color = MaterialTheme.colorScheme.primary)
            state.error != null -> Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
        }

        Button(onClick = onBack) {
            Text("Volver")
        }
    }
}

@Preview
@Composable
private fun LogScreenPreview() {
    MaterialTheme {
        LogContent(
            state = LogState(result = "LOG_RESULT_OK"),
            onServerUrlChange = {},
            onMessageChange = {},
            onLevelSelected = {},
            onSend = {},
            onBack = {}
        )
    }
}
