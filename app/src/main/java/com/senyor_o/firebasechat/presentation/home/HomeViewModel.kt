package com.senyor_o.firebasechat.presentation.home


import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.senyor_o.firebasechat.R

class HomeViewModel: ViewModel() {

    val state: MutableState<HomeState> = mutableStateOf(HomeState())

    fun logOut(context: Context) {
        val prefs = context.getSharedPreferences(context.getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()
        FirebaseAuth.getInstance().signOut()
        state.value = state.value.copy(successLogOut = true)
    }

    fun addMessage(
        message: String?,
        onSuccess: () -> Unit
    ) {
        if (!message.isNullOrBlank()) {
            Firebase.firestore.collection("messages").document().set(
                hashMapOf(
                    "message" to message,
                    "sent_by" to Firebase.auth.currentUser?.uid,
                    "sent_on" to System.currentTimeMillis()
                )
            ).addOnSuccessListener {
                onSuccess()
            }
        }
    }
    
}