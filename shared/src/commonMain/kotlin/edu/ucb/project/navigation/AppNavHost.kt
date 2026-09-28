package edu.ucb.project.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucb.project.catalogo.presentation.screen.CatalogScreen
import edu.ucb.project.login.presentation.screen.LoginScreen
import edu.ucb.project.userSearch.Presentation.screen.UserSearchScreen

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoute.Login) {
        composable<NavRoute.Login> {
            LoginScreen(navController = navController)
        }

        composable<NavRoute.UserSearch> {
            UserSearchScreen(navController = navController)
        }

        composable<NavRoute.Catalog> {
            CatalogScreen(navController = navController)
        }
    }
}
