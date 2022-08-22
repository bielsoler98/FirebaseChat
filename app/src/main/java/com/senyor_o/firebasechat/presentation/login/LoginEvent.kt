package com.senyor_o.firebasechat.presentation.login

sealed class LoginEvent {


    data class EmailEntered(val text: String) : LoginEvent()

    data class PasswordEntered(val text: String) : LoginEvent()

    object LoginWithGoogle : LoginEvent()

    object LoginInWithMail: LoginEvent()
}
