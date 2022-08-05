package com.senyor_o.firebasechat.presentation.login

import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.senyor_o.firebasechat.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    val state: MutableState<LoginState> = mutableStateOf(LoginState())

    fun login(email: String, password: String) {
        val errorMessage = if(email.isBlank() || password.isBlank()) {
            R.string.error_input_empty
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            R.string.error_not_a_valid_email
        } else null

        errorMessage?.let {
            state.value = state.value.copy(errorMessage = it)
            return
        }

        viewModelScope.launch {
            state.value = state.value.copy(displayProgressBar = true)

            FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        onLoginSuccess(it.result.user?.email!!)

                    } else {
                        state.value = state.value.copy(errorMessage = R.string.error_invalid_credentials)
                    }
                }

            state.value = state.value.copy(displayProgressBar = false)
        }
    }

    private fun onLoginSuccess(email: String) {
        state.value = state.value.copy(email = email)
        state.value = state.value.copy(successLogin = true)
    }

    fun hideErrorDialog() {
        state.value = state.value.copy(
            errorMessage = null
        )
    }

}