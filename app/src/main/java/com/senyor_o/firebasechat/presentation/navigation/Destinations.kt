package com.senyor_o.firebasechat.presentation.navigation

sealed class Destinations(
    val route: String,
) {
    object Login: Destinations("login")
    object Register: Destinations("register")
    object Home: Destinations("home")
}
