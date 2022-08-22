package com.senyor_o.firebasechat.presentation.home


import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senyor_o.firebasechat.R
import com.senyor_o.firebasechat.domain.model.Message
import com.senyor_o.firebasechat.domain.model.Response.*
import com.senyor_o.firebasechat.domain.model.User
import com.senyor_o.firebasechat.domain.repository.AuthRepository
import com.senyor_o.firebasechat.domain.repository.FirestoreRepository
import com.senyor_o.firebasechat.utils.Constants.IMAGE_TYPE
import com.senyor_o.firebasechat.utils.Constants.TEXT_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val firestoreRepo: FirestoreRepository,
): ViewModel() {

    val _state: MutableState<HomeState> = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val currentUser get() = repo.currentUser

    init {
        getMessages()
        currentUser?.let {
            getUserInfo(it.uid) { user ->
                _state.value = _state.value.copy(
                    displayProgressBar = false,
                    name = user.displayName,
                    profileImage = user.ppf
                )
            }
        }
    }

    private fun getMessages() {
        viewModelScope.launch {
            firestoreRepo.getMessages().collect { response ->
                when(response) {
                    is Error -> {
                        _state.value = _state.value.copy(
                            displayProgressBar = false,
                        )
                    }
                    Loading -> {
                        _state.value = _state.value.copy(
                            displayProgressBar = true
                        )
                    }
                    is Success -> {
                        response.data?.let { messages ->
                            _state.value = _state.value.copy(
                                displayProgressBar = false,
                            )
                            val messageList = mutableListOf<Pair<Message, User>>()
                            messages.forEach { message ->
                                getUserInfo(message.userUid) {
                                    messageList.add(Pair(message, it))
                                    _state.value = _state.value.copy(
                                        messages = messageList
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getUserInfo(
        userId: String,
        onSuccess: (User) -> Unit
    ) {
        viewModelScope.launch {
            firestoreRepo.getUserInfo(userId).collect { response ->
                when(response) {
                    is Error -> {
                        _state.value = _state.value.copy(
                            displayProgressBar = false
                        )
                    }
                    Loading -> {
                        _state.value = _state.value.copy(
                            displayProgressBar = true
                        )
                    }
                    is Success -> {
                        response.data?.let { user ->
                            onSuccess(user)
                        }
                    }
                }
            }
        }
    }

    private fun signOut() = viewModelScope.launch {
        repo.signOut().collect { response ->
            when(response) {
                is Loading ->  _state.value = state.value.copy(
                    displayProgressBar = true
                )
                is Success -> response.data?.let { signedOut ->
                    if (signedOut) {
                        _eventFlow.emit(UiEvent.UserLoggedOut)
                    }
                }
                is Error -> response.e?.let {
                    showError()
                }
            }
        }
    }

    private fun sendMessage(type:String, messageContent: String) = viewModelScope.launch {
        firestoreRepo.sendMessage(
            type,
            messageContent,
            _state.value.name,
            _state.value.profileImage
        ).collect { response ->
            when(response) {
                is Error -> response.e?.let {
                    showError()
                }
                Loading -> _state.value = state.value.copy(
                    messageContent = ""
                )
                is Success -> _state.value = state.value.copy(
                    displayProgressBar = false
                )
            }
        }
    }

    private fun uploadPhoto(
        imageUri: Uri,
        imageName: String,
        onSuccess: (String) -> Unit
    ) = viewModelScope.launch {
        firestoreRepo.uploadPhoto(
            imageUri,
            imageName,
            "image/png",
        ).collect { response ->
            when(response) {
                is Error -> {
                    _state.value = _state.value.copy(
                        displayProgressBar = false
                    )
                }
                Loading -> {
                    _state.value = _state.value.copy(
                        displayProgressBar = true
                    )
                }
                is Success -> {
                    response.data?.let {
                        onSuccess(it)
                    }
                }
            }
        }
    }

    private fun changePff(
        imageUrl: String
    ) = viewModelScope.launch {
        firestoreRepo.changeProfilePicture(
            imageUrl
        ).collect { response ->
            when(response) {
                is Error -> {
                    _state.value = _state.value.copy(
                        displayProgressBar = false
                    )
                }
                Loading -> {
                    _state.value = _state.value.copy(
                        displayProgressBar = true
                    )
                }
                is Success -> {
                    currentUser?.let {
                        getUserInfo(it.uid) { user ->
                            _state.value = _state.value.copy(
                                displayProgressBar = false,
                                name = user.displayName,
                                profileImage = user.ppf
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            HomeEvent.SendTextMessage -> {
                sendMessage(
                    TEXT_TYPE,
                    _state.value.messageContent
                )
            }
            is HomeEvent.SendImageMessage -> {
                uploadPhoto(
                    event.uri,
                    "${currentUser?.uid}-${System.currentTimeMillis()}"
                ) { imageUrl ->
                    sendMessage(
                        IMAGE_TYPE,
                        imageUrl
                    )
                }
            }
            HomeEvent.SignOut -> {
                signOut()
            }
            is HomeEvent.ContentMessageEntered -> {
                _state.value = _state.value.copy(
                    messageContent = event.text
                )
            }
            is HomeEvent.ChangePff -> {
                uploadPhoto(
                    event.uri,
                    "${currentUser?.uid}"
                ) {
                    changePff(it)
                }
            }
        }
    }

    private fun showError() {
        _state.value = state.value.copy(
            displayProgressBar = false,
            errorMessage = R.string.error_occurred
        )
    }

    fun hideErrorDialog() {
        _state.value = state.value.copy(
            errorMessage = null
        )
    }

    sealed class UiEvent {
        object UserLoggedOut: UiEvent()

        data class ShowGalleryIntent(val onSuccess: (Uri) -> Unit): UiEvent()
    }
}