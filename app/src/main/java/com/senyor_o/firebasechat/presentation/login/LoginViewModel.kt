package com.senyor_o.firebasechat.presentation.login

import android.content.Context
import android.util.Patterns
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.senyor_o.firebasechat.R
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

    fun oneTapGoogleSignIn(
        launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
        oneTapClient: SignInClient,
        context: Context
    ) {
        val signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(context.getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build()
                ).build()

        viewModelScope.launch {
            state.value = state.value.copy(displayGoogleProgressBar = true)
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener {
                    performAuthentication(
                        launcher,
                        it
                    )
                }.addOnFailureListener {
                    oneTapSignUp(
                        launcher,
                        oneTapClient,
                        context
                    )
                }
        }
    }

    private fun oneTapSignUp(
        launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
        oneTapClient: SignInClient,
        context: Context
    ) {

        val signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            ).build()

        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener {
                performAuthentication(
                    launcher,
                    it
                )
            }.addOnFailureListener {
                state.value = state.value.copy(errorMessage = R.string.error_google)
                state.value = state.value.copy(displayGoogleProgressBar = false)
            }
    }

    private fun performAuthentication(
        launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
        it: BeginSignInResult
    ) {
        val intent = IntentSenderRequest.Builder(it.pendingIntent.intentSender).build()
        launcher.launch(intent)
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

    fun loginWithCredentials(googleIdToken: String?) {
        val credential = GoogleAuthProvider.getCredential(googleIdToken, null)
        FirebaseAuth.getInstance()
            .signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onLoginSuccess(it.result.user?.email!!)
                    state.value = state.value.copy(displayGoogleProgressBar = false)
                } else {
                    state.value = state.value.copy(errorMessage = R.string.error_google)
                    state.value = state.value.copy(displayGoogleProgressBar = false)
                }
            }
    }

}