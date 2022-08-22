package com.senyor_o.firebasechat.presentation.login

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.senyor_o.firebasechat.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.senyor_o.firebasechat.R
import com.senyor_o.firebasechat.domain.model.Response
import com.senyor_o.firebasechat.domain.model.Response.*
import com.senyor_o.firebasechat.utils.Constants
import com.senyor_o.firebasechat.utils.Constants.SIGN_IN_ERROR_MESSAGE
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepository,
    val oneTapClient: SignInClient
): ViewModel() {

    val isUserAuthenticated get() = repo.isUserAuthenticatedInFirebase

    private val _state: MutableState<LoginState> = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getAuthState() = liveData(Dispatchers.IO) {
        repo.getFirebaseAuthState().collect { response ->
            emit(response)
        }
    }

    private fun oneTapSignIn() = viewModelScope.launch {
        repo.oneTapSignInWithGoogle().collect { response ->
            when(response) {
                is Error -> response.e?.let {
                    if (it.message == SIGN_IN_ERROR_MESSAGE) {
                        oneTapSignUp()
                    } else {
                        showGoogleErrorMessage()
                    }
                }
                Loading -> showGoogleProgressBar()
                is Success -> response.data?.let {
                    _eventFlow.emit(UiEvent.ShowGoogleIntent(it))
                }
            }
        }
    }

    private fun oneTapSignUp() = viewModelScope.launch {
        repo.oneTapSignUpWithGoogle().collect { response ->
            when(response) {
                is Error -> response.e?.let {
                    showGoogleErrorMessage()
                }
                Loading -> showGoogleProgressBar()
                is Success -> response.data?.let {
                    _eventFlow.emit(UiEvent.ShowGoogleIntent(it))
                }
            }
        }
    }

    fun signInWithGoogle(googleCredential: AuthCredential) = viewModelScope.launch {
        repo.firebaseSignInWithGoogle(googleCredential).collect { response ->
            when(response) {
                is Loading -> showGoogleProgressBar()
                is Success -> response.data?.let { isNewUser ->
                    if (isNewUser) {
                        createUser()
                    } else {
                        _eventFlow.emit(UiEvent.UserLoggedIn)
                    }
                }
                is Error -> response.e?.let {
                    showGoogleErrorMessage()
                }
            }
        }
    }

    private suspend fun signInWithMailAndPassword() = viewModelScope.launch {
        val email = _state.value.email
        val password = _state.value.password
        repo.logInWithMailAndPassword(email, password).collect { response ->
            when(response) {
                is Loading -> showProgressBar()
                is Success -> response.data?.let { isUserLoggedIn ->
                    if (isUserLoggedIn) {
                        _eventFlow.emit(UiEvent.UserLoggedIn)
                    }
                }
                is Error -> response.e?.let {
                    showLogInErrorMessage()
                }
            }
        }
    }

    private fun createUser() = viewModelScope.launch {
        repo.createUserInFirestore().collect { response ->
            when(response) {
                is Loading -> showGoogleProgressBar()
                is Success -> response.data?.let { isUserCreated ->
                    if (isUserCreated) {
                        _eventFlow.emit(UiEvent.UserLoggedIn)
                    }
                }
                is Error -> response.e?.let {
                    showGoogleErrorMessage()
                }
            }
        }
    }

    fun hideErrorDialog() {
        _state.value = _state.value.copy(
            errorMessage = null,
        )
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.LoginInWithMail -> viewModelScope.launch {
                showProgressBar()
                signInWithMailAndPassword()
            }
            is LoginEvent.LoginWithGoogle -> viewModelScope.launch {
                showGoogleProgressBar()
                oneTapSignIn()
            }
            is LoginEvent.EmailEntered -> {
                _state.value = _state.value.copy(
                    email = event.text
                )
            }
            is LoginEvent.PasswordEntered -> {
                _state.value = _state.value.copy(
                    password = event.text
                )
            }
        }
    }

    private fun showGoogleProgressBar() {
        _state.value = _state.value.copy(
            displayGoogleProgressBar = true
        )
    }

    fun hideGoogleProgressBar() {
        _state.value = _state.value.copy(
            displayGoogleProgressBar = false
        )
    }

    private fun showProgressBar() {
        _state.value = _state.value.copy(
            displayProgressBar = true
        )
    }

    fun showGoogleErrorMessage() {
        _state.value = _state.value.copy(
            errorMessage = R.string.error_google,
            displayGoogleProgressBar = false
        )
    }

    private fun showLogInErrorMessage() {
        _state.value = _state.value.copy(
            errorMessage = R.string.error_invalid_credentials,
            displayProgressBar = false,
        )
    }

    sealed class UiEvent {
        data class ShowGoogleIntent(val beginSignInResult: BeginSignInResult): UiEvent()

        object UserLoggedIn: UiEvent()
    }
}