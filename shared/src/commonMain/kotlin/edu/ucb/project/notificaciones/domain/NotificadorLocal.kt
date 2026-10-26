
package edu.ucb.project.notificaciones.domain

interface NotificadorLocal {
    fun permisoConcedido(): Boolean
    fun mostrarSaludo(): Boolean
    fun abrirAjustesDeNotificaciones()
}
