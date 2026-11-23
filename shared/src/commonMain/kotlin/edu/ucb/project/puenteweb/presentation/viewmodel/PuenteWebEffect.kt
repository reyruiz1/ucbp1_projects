package edu.ucb.project.puenteweb.presentation.viewmodel

import edu.ucb.project.navigation.NavRoute

sealed class PuenteWebEffect {
    data class AbrirPantalla(val ruta: NavRoute) : PuenteWebEffect()
}
