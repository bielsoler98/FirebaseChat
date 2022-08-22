package com.senyor_o.firebasechat.presentation.home

import android.net.Uri

sealed class HomeEvent {

    object SignOut : HomeEvent()

    object SendTextMessage : HomeEvent()

    data class SendImageMessage(val uri: Uri) : HomeEvent()

    data class ContentMessageEntered(val text: String) : HomeEvent()

    data class ChangePff(val uri: Uri) : HomeEvent()
}