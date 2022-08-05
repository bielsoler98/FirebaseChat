package com.senyor_o.firebasechat.presentation.home


import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.senyor_o.firebasechat.R

class HomeViewModel: ViewModel() {

    val state: MutableState<HomeState> = mutableStateOf(HomeState())

    fun logOut(context: Context) {
        val prefs = context.getSharedPreferences(context.getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()
        state.value = state.value.copy(successLogOut = true)
    }

    fun saveCredentials(context: Context, email: String) {
        val prefs = context.getSharedPreferences(context.getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        //provider?
        prefs.apply()
    }

}