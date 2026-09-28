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
}
