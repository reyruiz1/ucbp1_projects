package edu.ucb.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucb.project.Exam.presentation.screen.PeopleScreen
import edu.ucb.project.catalogo.presentation.screen.CatalogScreen
import edu.ucb.project.configlocal.presentation.screen.ConfigLocalScreen
import edu.ucb.project.log.presentation.screen.LogScreen
import edu.ucb.project.login.presentation.screen.LoginScreen
import edu.ucb.project.menu.presentation.screen.MenuScreen
import edu.ucb.project.notificaciones.presentation.screen.NotificacionesScreen
import edu.ucb.project.tareas.presentation.screen.TareasScreen
import edu.ucb.project.userSearch.Presentation.screen.UserSearchScreen

@Composable
fun AppNavHost(destino: String? = null) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoute.Menu) {
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

        composable<NavRoute.Menu> {
            MenuScreen(navController = navController)
        }

        composable<NavRoute.Tareas> {
            TareasScreen()
        }

        composable<NavRoute.Notificaciones> {
            NotificacionesScreen()
        }

        composable<NavRoute.ConfigLocal> {
            ConfigLocalScreen()
        }
    }

    LaunchedEffect(destino) {
        if (destino == Destinos.TAREAS) {
            navController.navigate(NavRoute.Tareas)
        }
    }
}
