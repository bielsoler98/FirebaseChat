package com.senyor_o.firebasechat.presentation.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.senyor_o.firebasechat.R
import com.senyor_o.firebasechat.domain.model.DrawerItem
import com.senyor_o.firebasechat.domain.model.Response.*
import com.senyor_o.firebasechat.presentation.components.*
import com.senyor_o.firebasechat.presentation.registration.RegisterViewModel
import com.senyor_o.firebasechat.ui.theme.FirebaseChatTheme
import com.senyor_o.firebasechat.utils.*
import com.senyor_o.firebasechat.utils.Constants.TEXT_TYPE
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToLoginScreen: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val state = viewModel.state.value

    LaunchedEffect(state.messages.size){
        listState.animateScrollToItem(
            if (state.messages.isNotEmpty()) {
                state.messages.size - 1
            } else {
                0
            }
        )
    }


    val addProfilePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                viewModel.onEvent(HomeEvent.ChangePff(it))
            }
        }
    val sendImageMessage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            viewModel.onEvent(HomeEvent.SendImageMessage(it))
        }
    }

    val items = listOf(
        DrawerItem(
            id = 1,
            title = "Log Out",
            icon = Icons.Default.Logout,
            onClick = {
                viewModel.onEvent(HomeEvent.SignOut)
            }
        )
    )

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                HomeViewModel.UiEvent.UserLoggedOut -> navigateToLoginScreen()
                is HomeViewModel.UiEvent.ShowGalleryIntent -> {
                    addProfilePictureLauncher.launch("image/*")
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.background(MaterialTheme.colors.background),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Toggle Drawer"
                        )
                    }
                }
            )
        },
        drawerBackgroundColor = MaterialTheme.colors.background,
        drawerContent = {
            DrawerHeader(
                state.name,
                state.profileImage
            ) {
                addProfilePictureLauncher.launch("image/*")
            }
            items.forEach { item ->
                DrawerMenuItem(
                    item = item,
                    onItemClick = {
                        item.onClick(it)
                        scope.launch { drawerState.close() }
                    }
                )
            }
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        bottomBar = {
            SendMessageBar(
                modifier = Modifier.padding(8.dp),
                textFieldValue = state.messageContent,
                textLabel = "Enter a message...",
                keyboardType = KeyboardType.Text,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                trailingIcon = {
                    Row {
                        IconButton(
                            onClick = {
                                sendImageMessage.launch("image/*")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachFile,
                                contentDescription = "Send Message"
                            )
                        }
                        Spacer(modifier = Modifier
                            .width(
                                8.dp
                            )
                        )
                        IconButton(
                            onClick = {
                                if(state.messageContent.isNotEmpty()) {
                                    viewModel.onEvent(HomeEvent.SendTextMessage)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send Message"
                            )
                        }
                    }

                },
                imeAction = ImeAction.Done,
                onValueChanged = {
                    viewModel.onEvent(HomeEvent.ContentMessageEntered(it))
                }
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier.fillMaxWidth().padding(innerPadding)
            ){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    state = listState
                ) {
                    state.messages.sortedBy { it.first.sentDate }.forEach { messagePair ->
                        item {
                            MessageItem(
                                message = messagePair.first,
                                user = messagePair.second,
                                isCurrentUser = messagePair.second.uid == viewModel.currentUser?.uid,
                            )
                        }
                    }
                }
            }
            if(state.displayProgressBar) {
                ProgressBar()
            }

            if(state.errorMessage != null) {
                EventDialog(errorMessage = state.errorMessage,
                    onDismiss = {
                        viewModel.hideErrorDialog()
                    }
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FirebaseChatTheme {
        HomeScreen(viewModel = hiltViewModel(), navigateToLoginScreen = {})
    }
}