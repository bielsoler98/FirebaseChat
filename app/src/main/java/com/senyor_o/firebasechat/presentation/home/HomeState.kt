package com.senyor_o.firebasechat.presentation.home

import android.net.Uri
import androidx.annotation.StringRes

data class HomeState(
    val name: String = "",
    val profileImage: String? = null,
    val messages: MutableList<Map<String, Any>> = mutableListOf(),
    val successLogOut: Boolean = false,
    val displayProgressBar: Boolean = false,
    @StringRes val errorMessage: Int? = null,

)
