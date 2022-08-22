package com.senyor_o.firebasechat.presentation.registration

import androidx.annotation.StringRes
import com.senyor_o.firebasechat.domain.model.Response

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val displayProgressBar: Boolean = false,
    @StringRes val errorMessage: Int? = null
)
