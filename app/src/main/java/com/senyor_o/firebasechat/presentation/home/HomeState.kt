package com.senyor_o.firebasechat.presentation.home

import androidx.annotation.StringRes
import com.senyor_o.firebasechat.domain.model.Message
import com.senyor_o.firebasechat.domain.model.Response
import com.senyor_o.firebasechat.domain.model.User

data class HomeState(
    val name: String ="",
    val profileImage: String = "",
    val messageContent: String = "",
    val messages: List<Pair<Message, User>> = emptyList(),
    val displayProgressBar: Boolean = false,
    @StringRes val errorMessage: Int? = null,
)
