package edu.ucb.project.navigation
import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoute {
    @Serializable
    object Login : NavRoute()

    @Serializable
    object UserSearch : NavRoute()

    @Serializable
    object Catalog : NavRoute()

    @Serializable
    object People : NavRoute()

    @Serializable
    object Logs : NavRoute()

    @Serializable
    object Menu : NavRoute()

    @Serializable
    object Tareas : NavRoute()

    @Serializable
    object Notificaciones : NavRoute()

    @Serializable
    object ConfigLocal : NavRoute()
}
