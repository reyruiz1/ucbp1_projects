package edu.ucb.project.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucb.project.Exam.presentation.screen.PeopleScreen
import edu.ucb.project.catalogo.presentation.screen.CatalogScreen
import edu.ucb.project.log.presentation.screen.LogScreen
import edu.ucb.project.login.presentation.screen.LoginScreen
import edu.ucb.project.userSearch.Presentation.screen.UserSearchScreen
import edu.ucb.project.notificaciones.presentation.screen.NotificacionesScreen

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoute.Login) {
        composable<NavRoute.Notificaciones> {
            NotificacionesScreen(
                onVolver = { navController.popBackStack() }
            )
        }
        composable<NavRoute.Login> {
            LoginScreen(navController = navController)
        }

        composable<NavRoute.UserSearch> {
            UserSearchScreen(navController = navController)
        }

        composable<NavRoute.Catalog> {
            CatalogScreen(navController = navController)
        }

        composable<NavRoute.People> {
            PeopleScreen(navController = navController)
        }

        composable<NavRoute.Logs> {
            LogScreen(navController = navController)
        }
    }
}
