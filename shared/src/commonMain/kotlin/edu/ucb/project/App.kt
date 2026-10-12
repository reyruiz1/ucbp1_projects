package edu.ucb.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import edu.ucb.project.navigation.AppNavHost
import edu.ucb.project.exchange.presentation.screen.ExchangeScreen
import edu.ucb.project.movies.presentation.screen.DollarScreen

@Composable
fun App() {
    MaterialTheme {
        AppNavHost()
        ExchangeScreen()
        DollarScreen()
    }
}
