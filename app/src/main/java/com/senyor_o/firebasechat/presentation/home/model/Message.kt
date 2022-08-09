package com.senyor_o.firebasechat.presentation.home.model

import androidx.lifecycle.MutableLiveData

data class Message(
    val contentType: String,
    val content: String,
    val user: MutableLiveData<User>,
    val sentDate: Long,
    val isCurrentUser: Boolean,
)
