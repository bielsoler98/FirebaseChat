package com.senyor_o.firebasechat.presentation.home

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.senyor_o.firebasechat.presentation.components.RoundedButton
import com.senyor_o.firebasechat.presentation.login.LoginState
import com.senyor_o.firebasechat.presentation.registration.RegisterState
import com.senyor_o.firebasechat.ui.theme.FirebaseChatTheme

@Composable
fun HomeScreen(
    state: HomeState,
    onLogOut: (Context) -> Unit,
    onEnter: (Context, String) -> Unit
) {
    val context = LocalContext.current
    onEnter(context, state.email)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Home Screen",
            style = MaterialTheme.typography.displaySmall
        )

        Text("Email: ${state.email}")

        RoundedButton(
            text = "Log Out",
            onClick = {
                onLogOut(context)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FirebaseChatTheme {
        HomeScreen(
            state = HomeState(),
            onLogOut = {},
            onEnter = { _ , _  -> }
        )
    }
}