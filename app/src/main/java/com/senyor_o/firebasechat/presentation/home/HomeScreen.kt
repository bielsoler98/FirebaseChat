package com.senyor_o.firebasechat.presentation.home

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senyor_o.firebasechat.presentation.components.RoundedButton
import com.senyor_o.firebasechat.presentation.components.SendMesssageBar
import com.senyor_o.firebasechat.presentation.components.TransparentTextField
import com.senyor_o.firebasechat.presentation.login.LoginState
import com.senyor_o.firebasechat.presentation.registration.RegisterState
import com.senyor_o.firebasechat.ui.theme.FirebaseChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
) {
    val focusManager = LocalFocusManager.current
    val messageValue = rememberSaveable{ mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Text(
//            "Home Screen",
//            style = MaterialTheme.typography.displaySmall
//        )
//
//        Text("Email: ${state.email}")

//        RoundedButton(
//            text = "Log Out",
//            onClick = {
//                onLogOut(context)
//            }
//        )
        SendMesssageBar(
            textFieldValue = messageValue,
            textLabel = "Enter a message...",
            keyboardType = KeyboardType.Text,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            trailingIcon = {
                IconButton(
                    onClick = {
                        viewModel.addMessage(
                            messageValue.value,
                        onSuccess = {
                            messageValue.value = ""
                        }
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Message"
                    )
                }
            },
            imeAction = ImeAction.Done
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FirebaseChatTheme {
        HomeScreen(viewModel = hiltViewModel())
    }
}