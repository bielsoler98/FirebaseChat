package com.senyor_o.firebasechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.senyor_o.firebasechat.ui.theme.FirebaseChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseChatApp()
        }
    }
}

@Composable
fun FirebaseChatApp() {
    var currentScreen: ChatDestination by remember {
        mutableStateOf(Registration)
    }
    FirebaseChatTheme {
        currentScreen.screen()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FirebaseChatApp()
}