package com.senyor_o.firebasechat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.senyor_o.firebasechat.presentation.chat_list.ChatListScreen
import com.senyor_o.firebasechat.presentation.login.LoginScreen
import com.senyor_o.firebasechat.presentation.registration.RegistrationScreen

interface ChatDestination {
    val icon: ImageVector
    val route: String
    val screen: @Composable () -> Unit
}

object ChatList : ChatDestination {
    override val icon: ImageVector
        get() = Icons.Rounded.Home
    override val route: String
        get() = "chat_list"
    override val screen: @Composable () -> Unit
        get() = { ChatListScreen() }
}

object Login : ChatDestination {
    override val icon: ImageVector
        get() = Icons.Rounded.Home
    override val route: String
        get() = "login"
    override val screen: @Composable () -> Unit
        get() = { LoginScreen() }
}

object Registration : ChatDestination {
    override val icon: ImageVector
        get() = Icons.Rounded.Home
    override val route: String
        get() = "login"
    override val screen: @Composable () -> Unit
        get() = { RegistrationScreen() }
}