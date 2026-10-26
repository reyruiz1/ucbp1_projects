
package edu.ucb.project.notificaciones.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.ucb.project.notificaciones.domain.NotificadorLocal
import org.koin.compose.koinInject

@Composable
fun NotificacionesScreen(
    onVolver: () -> Unit,
    notificador: NotificadorLocal = koinInject()
) {
    var permiso by remember {
        mutableStateOf(notificador.permisoConcedido())
    }

    var enviadas by remember { mutableIntStateOf(0) }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(
            12.dp,
            Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "E25-5 · Notificaciones locales",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = if (permiso) {
                "Notificaciones: HABILITADAS"
            } else {
                "Notificaciones: BLOQUEADAS"
            },
            color = if (permiso) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.error
            }
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                permiso = notificador.permisoConcedido()
                if (permiso && notificador.mostrarSaludo()) {
                    enviadas++
                    mensaje = "Notificación enviada"
                } else {
                    mensaje = "Revisa el permiso y los ajustes"
                }
            }
        ) {
            Text("Enviar Hola desde la app")
        }

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                permiso = notificador.permisoConcedido()
            }
        ) {
            Text("Actualizar estado del permiso")
        }

        if (!permiso) {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    notificador.abrirAjustesDeNotificaciones()
                }
            ) {
                Text("Abrir ajustes de notificaciones")
            }
        }

        Text("Notificaciones enviadas: $enviadas")

        if (mensaje.isNotEmpty()) {
            Text(mensaje)
        }

        OutlinedButton(onClick = onVolver) {
            Text("Volver")
        }
    }
}
