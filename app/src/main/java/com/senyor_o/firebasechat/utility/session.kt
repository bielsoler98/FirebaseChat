package com.senyor_o.firebasechat.utility

import android.content.Context
import com.senyor_o.firebasechat.R

fun session(context: Context): String? {
    val prefs =
        context.getSharedPreferences(context.getString(R.string.prefs_file), Context.MODE_PRIVATE)

    return prefs?.getString("email", null)
}