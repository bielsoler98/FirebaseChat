package com.senyor_o.firebasechat.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.senyor_o.firebasechat.domain.model.Message
import com.senyor_o.firebasechat.domain.model.Response
import com.senyor_o.firebasechat.domain.model.Response.*
import com.senyor_o.firebasechat.domain.model.User
import com.senyor_o.firebasechat.domain.repository.FirestoreRepository
import com.senyor_o.firebasechat.utils.Constants
import com.senyor_o.firebasechat.utils.Constants.DISPLAY_NAME
import com.senyor_o.firebasechat.utils.Constants.MESSAGES
import com.senyor_o.firebasechat.utils.Constants.PHOTO_URL
import com.senyor_o.firebasechat.utils.Constants.SENT_DATE
import com.senyor_o.firebasechat.utils.Constants.USERS_REF
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@ExperimentalCoroutinesApi
class FirestoreRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
): FirestoreRepository {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun sendMessage(
        type: String,
        message: String,
        userName: String,
        pff: String
    ): Flow<Response<Void?>> = flow {
        try {
            emit(Loading)
            currentUser?.apply {
                val messageId = db.collection(MESSAGES).document().id
                val message = Message(
                    contentType = type,
                    content = message,
                    userUid = uid,
                    sentDate = System.currentTimeMillis(),
                )
                val addition = db.collection(MESSAGES).document(messageId).set(message).await()
                emit(Success(addition))
            }
        } catch (e: Exception) {
            emit(Error(e))
        }
    }

    override suspend fun changeProfilePicture(imageUrl: String): Flow<Response<Void?>> = flow {
        try {
            emit(Loading)
            currentUser?.apply {
                val addition = db.collection(USERS_REF).document(uid).update(
                    hashMapOf<String, Any?>(
                        PHOTO_URL to imageUrl
                    )
                ).await()
                emit(Success(addition))
            }
        } catch (e: Exception) {
            emit(Error(e))
        }
    }

    override suspend fun uploadPhoto(imageUri: Uri, name: String, mimeType: String?): Flow<Response<String>> = flow {
        try {
            emit(Loading)
            val storageRef = storage.reference

            val fileRef = storageRef.child("images/$name")
            val metadata = mimeType?.let {
                StorageMetadata.Builder()
                    .setContentType(mimeType)
                    .build()
            }
            if(metadata != null) {
                fileRef.putFile(imageUri, metadata).await()
            } else {
                fileRef.putFile(imageUri).await()
            }
            val downloadUrl = fileRef.downloadUrl.await().toString()
            emit(Success(downloadUrl))
        } catch (e: Exception) {
            emit(Error(e))
        }
    }

    override suspend fun getMessages(): Flow<Response<List<Message>>> = callbackFlow {
        val snapshotListener = db.collection(MESSAGES).orderBy(SENT_DATE).addSnapshotListener { snapshot, e ->
            val response = if(snapshot != null) {
                val messages: List<Message> = snapshot.toObjects(Message::class.java)
                Success(messages)
            } else {
                Error(e)
            }
            trySend(response).isSuccess
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun getUserInfo(userId: String): Flow<Response<User>> = flow {
        try {
            emit(Loading)
            val userDocument = db.collection(USERS_REF)
                .document(userId)
                .get().await()
            val user = userDocument.toObject(User::class.java)
            emit(Success(user))
        } catch (e: Exception) {
            emit(Error(e))
        }
    }

}