package com.senyor_o.firebasechat.presentation.registration

sealed class RegisterEvent {

    data class NameEntered(val text: String) : RegisterEvent()

    data class EmailEntered(val text: String) : RegisterEvent()

    data class PasswordEntered(val text: String) : RegisterEvent()

    data class ConfirmPasswordEntered(val text: String) : RegisterEvent()

    object RegisterUser : RegisterEvent()
}