package com.senyor_o.firebasechat.presentation.splash


data class SplashState(
    val email: String = "",
    val successSession: Boolean = false,
    val sessionRetrieved: Boolean = false
)
