package com.senyor_o.firebasechat.presentation.login

import androidx.annotation.StringRes
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.senyor_o.firebasechat.domain.model.Response
import com.senyor_o.firebasechat.domain.model.Response.*

data class LoginState(
    val email: String = "",
    val password: String = "",
    val displayProgressBar: Boolean = false,
    val displayGoogleProgressBar: Boolean = false,
    @StringRes val errorMessage: Int? = null
)
