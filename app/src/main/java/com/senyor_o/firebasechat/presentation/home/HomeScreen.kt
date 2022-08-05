package com.senyor_o.firebasechat.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.senyor_o.firebasechat.ui.theme.FirebaseChatTheme

@Composable
fun HomeScreen(
    email: String,
    password: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Home Screen",
            style = MaterialTheme.typography.displaySmall
        )

        Text("Email: $email, password: $password")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FirebaseChatTheme {
        HomeScreen(
            email = "test@test.com",
            password = "test"
        )
    }
}