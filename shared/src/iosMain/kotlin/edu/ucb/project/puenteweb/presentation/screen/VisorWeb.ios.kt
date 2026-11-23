package edu.ucb.project.puenteweb.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow

@Composable
actual fun VisorWeb(
    url: String,
    cartasSalientes: Flow<String>,
    onCartaRecibida: (String) -> Unit,
    onCanalCerrado: () -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text("La cartelera web por paso de mensajes solo está disponible en Android")
    }
}
