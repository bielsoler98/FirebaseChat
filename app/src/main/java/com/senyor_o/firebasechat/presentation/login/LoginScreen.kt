package com.senyor_o.firebasechat.presentation.login

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.senyor_o.firebasechat.R
import com.senyor_o.firebasechat.presentation.components.RoundedButton
import com.senyor_o.firebasechat.presentation.components.TransparentTextField
import com.senyor_o.firebasechat.presentation.components.EventDialog
import com.senyor_o.firebasechat.presentation.components.SocialMediaButton
import com.senyor_o.firebasechat.ui.theme.FACEBOOKCOLOR
import com.senyor_o.firebasechat.ui.theme.FirebaseChatTheme
import com.senyor_o.firebasechat.ui.theme.GMAILCOLOR
import com.senyor_o.firebasechat.utility.session

@ExperimentalMaterial3Api
@Composable
fun LoginScreen(
    state: LoginState,
    onLogin: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit,
    onDismissDialog: () -> Unit
) {
    val emailValue = rememberSaveable{ mutableStateOf("") }
    val passwordValue = rememberSaveable{ mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Login Image",
            contentScale = ContentScale.Inside
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ){
            ConstraintLayout {

                val (surface, fab) = createRefs()

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(550.dp)
                        .constrainAs(surface) {
                            bottom.linkTo(parent.bottom)
                        },
                    color = Color.White,
                    shape = RoundedCornerShape(
                        topStartPercent = 8,
                        topEndPercent = 8
                    )
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ){
                        Text(
                            text = "Welcome Back!",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )

                        Text(
                            text = "Login to your Account",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = MaterialTheme.colorScheme.primary
                            )
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ){
                            TransparentTextField(
                                textFieldValue = emailValue,
                                textLabel = "Email",
                                keyboardType = KeyboardType.Email,
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                ),
                                imeAction = ImeAction.Next
                            )

                            TransparentTextField(
                                textFieldValue = passwordValue,
                                textLabel = "Password",
                                keyboardType = KeyboardType.Password,
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        focusManager.clearFocus()

                                        onLogin(emailValue.value, passwordValue.value)
                                    }
                                ),
                                imeAction = ImeAction.Done,
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            passwordVisibility = !passwordVisibility
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if(passwordVisibility) {
                                                Icons.Default.Visibility
                                            } else {
                                                Icons.Default.VisibilityOff
                                            },
                                            contentDescription = "Toggle Password Icon"
                                        )
                                    }
                                },
                                visualTransformation = if(passwordVisibility) {
                                    VisualTransformation.None
                                } else {
                                    PasswordVisualTransformation()
                                }
                            )

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Forgot Password?",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.End
                            )
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RoundedButton(
                                text = "Login",
                                displayProgressBar = state.displayProgressBar,
                                onClick = {
                                    onLogin(emailValue.value, passwordValue.value)
                                }
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ){
                                Divider(
                                    modifier = Modifier.width(24.dp),
                                    thickness = 1.dp,
                                    color = Color.Gray
                                )

                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    text = "OR",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Black
                                    )
                                )

                                Divider(
                                    modifier = Modifier.width(24.dp),
                                    thickness = 1.dp,
                                    color = Color.Gray
                                )
                            }

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Login with",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    MaterialTheme.colorScheme.primary
                                ),
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            SocialMediaButton(
                                text = "Login with Facebook",
                                onClick = {  },
                                socialMediaColor = FACEBOOKCOLOR
                            )

                            SocialMediaButton(
                                text = "Login with Gmail",
                                onClick = { },
                                socialMediaColor = GMAILCOLOR
                            )

                            ClickableText(
                                text = buildAnnotatedString {
                                    append("Do not have an Account? ")

                                    withStyle(
                                        style = SpanStyle(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    ){
                                        append("Sign up")
                                    }
                                }
                            ){
                                onNavigateToRegister()
                            }
                        }
                    }
                }

                FloatingActionButton(
                    modifier = Modifier
                        .size(72.dp)
                        .constrainAs(fab) {
                            top.linkTo(surface.top, margin = (-36).dp)
                            end.linkTo(surface.end, margin = 36.dp)
                        },
                    onClick = {
                        onNavigateToRegister()
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        modifier = Modifier.size(42.dp),
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Forward Icon",
                        tint = Color.White
                    )
                }
            }
        }

        if(state.errorMessage != null){
            EventDialog(
                errorMessage = state.errorMessage,
                onDismiss = onDismissDialog
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    FirebaseChatTheme {
        LoginScreen(
            state = LoginState(),
            onLogin = { _ , _  -> },
            onNavigateToRegister = { },
            onDismissDialog = {  }
        )
    }
}













