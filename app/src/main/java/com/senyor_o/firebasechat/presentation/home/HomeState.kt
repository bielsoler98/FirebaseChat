package com.senyor_o.firebasechat.presentation.home

import androidx.annotation.StringRes

data class HomeState(
    val email: String = "",
    val successLogOut: Boolean = false,
    val displayProgressBar: Boolean = false,
    @StringRes val errorMessage: Int? = null
)
