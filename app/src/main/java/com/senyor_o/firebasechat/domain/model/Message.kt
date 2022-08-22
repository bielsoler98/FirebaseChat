package com.senyor_o.firebasechat.domain.model

data class Message(
    val contentType: String = "",
    val content: String = "",
    val sentDate: Long = 0L,
    val userUid: String = ""
)
