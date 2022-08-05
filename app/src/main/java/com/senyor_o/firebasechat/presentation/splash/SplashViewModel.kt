package com.senyor_o.firebasechat.presentation.splash

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senyor_o.firebasechat.R
import com.senyor_o.firebasechat.presentation.login.LoginState
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {

    val state: MutableState<SplashState> = mutableStateOf(SplashState())

    fun validateSession(context: Context) {
        val prefs =
            context.getSharedPreferences(context.getString(R.string.prefs_file), Context.MODE_PRIVATE)
        viewModelScope.launch {
            val email = prefs?.getString("email", null)

            email?.let {
                state.value = state.value.copy(email = it, successSession = true)
            }
            state.value = state.value.copy(sessionRetrieved = true)
        }
    }
}