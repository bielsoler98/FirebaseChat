package com.senyor_o.firebasechat.domain.model

import com.google.firebase.firestore.FieldValue

data class User(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val ppf: String = "",
)