package edu.ucb.project.puenteweb.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow

@Composable
expect fun VisorWeb(
    url: String,
    cartasSalientes: Flow<String>,
    onCartaRecibida: (String) -> Unit,
    onCanalCerrado: () -> Unit,
    modifier: Modifier = Modifier
)
