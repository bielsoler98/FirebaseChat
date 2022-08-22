package com.senyor_o.firebasechat.presentation.registration

import android.util.Patterns
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senyor_o.firebasechat.R
import com.senyor_o.firebasechat.domain.model.Response
import com.senyor_o.firebasechat.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repo: AuthRepository,
): ViewModel() {

    private val _state: MutableState<RegisterState> = mutableStateOf(RegisterState())
    val state: State<RegisterState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    suspend fun register(){

        val name = _state.value.name
        val email = _state.value.email
        val password = _state.value.password
        val confirmPassword = _state.value.confirmPassword

        val errorMessage = if(name.isBlank() || email.isBlank() ||  password.isBlank() || confirmPassword.isBlank()){
            R.string.error_input_empty
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            R.string.error_not_a_valid_email
        } else if(password != confirmPassword) {
            R.string.error_incorrectly_repeated_password
        } else null

        errorMessage?.let {
            _state.value = state.value.copy(errorMessage = errorMessage)
            return
        }

        repo.createUserWithEmailAndPassword(name, email, password).collect { response ->
            when(response) {
                is Response.Loading -> _state.value = state.value.copy(
                    displayProgressBar = true
                )
                is Response.Success -> response.data?.let { isUserCreated ->
                    if (isUserCreated) {
                        _eventFlow.emit(UiEvent.UserCreated)
                    }
                }
                is Response.Error -> response.e?.let {
                    showError()
                }
            }
        }
    }

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.ConfirmPasswordEntered -> {
                _state.value = _state.value.copy(
                    confirmPassword = event.text
                )
            }
            is RegisterEvent.EmailEntered -> {
                _state.value = _state.value.copy(
                    email = event.text
                )
            }
            is RegisterEvent.NameEntered -> {
                _state.value = _state.value.copy(
                    name = event.text
                )
            }
            is RegisterEvent.PasswordEntered -> {
                _state.value = _state.value.copy(
                    password = event.text
                )
            }
            RegisterEvent.RegisterUser -> viewModelScope.launch {
                register()
            }
        }
    }

    fun hideErrorDialog() {
        _state.value = state.value.copy(
            errorMessage = null
        )
    }

    fun showError() {
        _state.value = state.value.copy(
            errorMessage = R.string.error_occurred,
            displayProgressBar = false
        )
    }

    sealed class UiEvent {
        object UserCreated: UiEvent()
    }
}