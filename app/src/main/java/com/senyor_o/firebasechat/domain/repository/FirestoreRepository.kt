package com.senyor_o.firebasechat.domain.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.senyor_o.firebasechat.domain.model.Message
import com.senyor_o.firebasechat.domain.model.Response
import com.senyor_o.firebasechat.domain.model.User
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    val currentUser: FirebaseUser?

    suspend fun sendMessage(type: String, message: String, userName: String, pff: String): Flow<Response<Void?>>

    suspend fun changeProfilePicture(imageUrl: String): Flow<Response<Void?>>

    suspend fun uploadPhoto(imageUri: Uri, name: String, mimeType: String?): Flow<Response<String>>

    suspend fun getMessages(): Flow<Response<List<Message>>>

    suspend fun getUserInfo(userId: String): Flow<Response<User>>
}