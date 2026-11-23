package edu.ucb.project.puenteweb.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.ucb.project.puenteweb.presentation.viewmodel.Direccion
import edu.ucb.project.puenteweb.presentation.viewmodel.EntradaBitacora
import edu.ucb.project.puenteweb.presentation.viewmodel.PuenteWebEffect
import edu.ucb.project.puenteweb.presentation.viewmodel.PuenteWebEvent
import edu.ucb.project.puenteweb.presentation.viewmodel.PuenteWebState
import edu.ucb.project.puenteweb.presentation.viewmodel.PuenteWebViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PuenteWebScreen(
    navController: NavHostController,
    viewModel: PuenteWebViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PuenteWebEffect.AbrirPantalla -> navController.navigate(effect.ruta)
            }
        }
    }

    MaterialTheme(colorScheme = if (state.modoOscuro) darkColorScheme() else lightColorScheme()) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Encabezado(
                    conectado = state.conectado,
                    oscuro = state.modoOscuro,
                    onRecargar = { viewModel.emitEvent(PuenteWebEvent.OnRecargar) },
                    onUrl = { viewModel.emitEvent(PuenteWebEvent.OnAbrirUrl) }
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (state.url.isBlank()) {
                        Text(
                            "Toca \"URL\" y escribe la dirección pública (ngrok) de tu página web",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(24.dp)
                        )
                    } else {
                        key(state.url, state.recargas) {
                            VisorWeb(
                                url = state.url,
                                cartasSalientes = viewModel.cartasSalientes,
                                onCartaRecibida = { viewModel.emitEvent(PuenteWebEvent.OnCartaRecibida(it)) },
                                onCanalCerrado = { viewModel.emitEvent(PuenteWebEvent.OnCanalCerrado) },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                HorizontalDivider()

                PanelNativo(state = state, onEvent = viewModel::emitEvent)
            }
        }

        if (state.editandoUrl) {
            DialogoUrl(state = state, onEvent = viewModel::emitEvent)
        }

        state.aviso?.let { aviso ->
            AlertDialog(
                onDismissRequest = { viewModel.emitEvent(PuenteWebEvent.OnCerrarAviso) },
                title = { Text("Respuesta de la web") },
                text = { Text(aviso) },
                confirmButton = {
                    TextButton(onClick = { viewModel.emitEvent(PuenteWebEvent.OnCerrarAviso) }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
private fun Encabezado(
    conectado: Boolean,
    oscuro: Boolean,
    onRecargar: () -> Unit,
    onUrl: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Cartelera web", style = MaterialTheme.typography.titleLarge)
            Text(
                if (conectado) "● Conectada" else "○ Esperando a la web",
                style = MaterialTheme.typography.labelMedium,
                color = if (conectado) colorApp(oscuro) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        TextButton(onClick = onRecargar) { Text("Recargar") }
        TextButton(onClick = onUrl) { Text("URL") }
    }
}

@Composable
private fun PanelNativo(
    state: PuenteWebState,
    onEvent: (PuenteWebEvent) -> Unit
) {
    val listaBitacora = rememberLazyListState()
    val ultimaEntrada = state.bitacora.firstOrNull()?.numero

    LaunchedEffect(ultimaEntrada, state.bitacoraVisible) {
        if (state.bitacoraVisible && ultimaEntrada != null) listaBitacora.scrollToItem(0)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Modo oscuro", modifier = Modifier.weight(1f))
            Switch(
                checked = state.modoOscuro,
                onCheckedChange = { onEvent(PuenteWebEvent.OnModoOscuroChange(it)) }
            )
        }

        Button(
            onClick = { onEvent(PuenteWebEvent.OnPedirSeleccion) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿Qué película eligió en la web?")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Bitácora nativa",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.weight(1f)
            )
            if (state.bitacoraVisible) {
                TextButton(onClick = { onEvent(PuenteWebEvent.OnLimpiarBitacora) }) {
                    Text("Limpiar")
                }
            }
            TextButton(onClick = { onEvent(PuenteWebEvent.OnAlternarBitacora) }) {
                Text("${if (state.bitacoraVisible) "Ocultar" else "Mostrar"} (${state.bitacora.size})")
            }
        }

        if (state.bitacoraVisible) {
            LazyColumn(
                state = listaBitacora,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {
                items(state.bitacora, key = { it.numero }) { entrada ->
                    LineaBitacora(entrada, state.modoOscuro)
                }
            }
        }
    }
}

@Composable
private fun LineaBitacora(entrada: EntradaBitacora, oscuro: Boolean) {
    var abierta by remember { mutableStateOf(false) }
    val color = when (entrada.direccion) {
        Direccion.WEB_A_APP -> colorWeb(oscuro)
        Direccion.APP_A_WEB -> colorApp(oscuro)
        Direccion.SISTEMA -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Text(
        text = "#${entrada.numero} ${entrada.direccion.etiqueta}  ${entrada.detalle}",
        color = color,
        fontFamily = FontFamily.Monospace,
        style = MaterialTheme.typography.bodySmall,
        maxLines = if (abierta) Int.MAX_VALUE else 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { abierta = !abierta }
            .padding(vertical = 2.dp)
    )
}

@Composable
private fun DialogoUrl(
    state: PuenteWebState,
    onEvent: (PuenteWebEvent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onEvent(PuenteWebEvent.OnCerrarUrl) },
        title = { Text("URL de la página web") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "La dirección solo se ve aquí. La cartelera nunca la muestra.",
                    style = MaterialTheme.typography.bodySmall
                )
                OutlinedTextField(
                    value = state.urlEditable,
                    onValueChange = { onEvent(PuenteWebEvent.OnUrlChange(it)) },
                    singleLine = true,
                    placeholder = { Text("https://xxxx.ngrok-free.app") },
                    isError = state.errorUrl != null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { onEvent(PuenteWebEvent.OnGuardarUrl) }),
                    supportingText = state.errorUrl?.let { error -> { Text(error) } }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onEvent(PuenteWebEvent.OnGuardarUrl) }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = { onEvent(PuenteWebEvent.OnCerrarUrl) }) { Text("Cancelar") }
        }
    )
}

private fun colorWeb(oscuro: Boolean) = if (oscuro) Color(0xFFF5A524) else Color(0xFFD9730D)

private fun colorApp(oscuro: Boolean) = if (oscuro) Color(0xFF3CC4AE) else Color(0xFF0F7B6C)
