package com.senyor_o.firebasechat.data.repository

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.senyor_o.firebasechat.domain.model.Response.*
import com.senyor_o.firebasechat.domain.model.User
import com.senyor_o.firebasechat.domain.repository.AuthRepository
import com.senyor_o.firebasechat.utils.Constants.CREATED_AT
import com.senyor_o.firebasechat.utils.Constants.DISPLAY_NAME
import com.senyor_o.firebasechat.utils.Constants.EMAIL
import com.senyor_o.firebasechat.utils.Constants.PHOTO_URL
import com.senyor_o.firebasechat.utils.Constants.SIGN_IN_REQUEST
import com.senyor_o.firebasechat.utils.Constants.SIGN_UP_REQUEST
import com.senyor_o.firebasechat.utils.Constants.USERS_REF
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private var signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private var signUpRequest: BeginSignInRequest,
    private var signInClient: GoogleSignInClient,
    private val db: FirebaseFirestore
) : AuthRepository {

    override val isUserAuthenticatedInFirebase: Boolean
        get() = auth.currentUser != null

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun oneTapSignInWithGoogle() = flow {
        try {
            emit(Loading)
            val result = oneTapClient.beginSignIn(signInRequest).await()
            emit(Success(result))
        } catch (e: Exception) {
            emit(Error(e))
        }
    }

    override suspend fun oneTapSignUpWithGoogle() = flow {
        try {
            emit(Loading)
            val result = oneTapClient.beginSignIn(signUpRequest).await()
            emit(Success(result))
        } catch (e: Exception) {
            emit(Error(e))
        }
    }

    override suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential) = flow {
        try {
            emit(Loading)
            val authResult = auth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser
            emit(Success(isNewUser))
        } catch (e: Exception) {
            emit(Error(e))
        }
    }

    override suspend fun logInWithMailAndPassword(email: String, password: String) = flow {
        try {
            emit(Loading)
            auth.signInWithEmailAndPassword(email, password).await()
            emit(Success(true))
        } catch (e: Exception) {
            emit(Error(e))
        }
    }

    override suspend fun createUserWithEmailAndPassword(name: String, email: String, password: String) = flow {
        try {
            emit(Loading)
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            authResult.user?.apply {
                val user = User(
                    uid = uid,
                    displayName = name,
                    email = email,
                    ppf = "",
//                    createdAt = FieldValue.serverTimestamp()
                )
                db.collection(USERS_REF).document(uid).set(user).await()
            emit(Success(true))
            }
        } catch (e: Exception) {
            emit(Error(e))
        }
    }

    override suspend fun createUserInFirestore() = flow {
        try {
            emit(Loading)
            auth.currentUser?.apply {
                val user = User(
                    uid = uid,
                    displayName = displayName ?: "",
                    email = email ?: "",
                    ppf = photoUrl?.toString() ?: "",
//                    createdAt = FieldValue.serverTimestamp()
                )
                db.collection(USERS_REF).document(uid).set(user).await()
                emit(Success(true))
            }
        } catch (e: Exception) {
            emit(Error(e))
        }
    }

    override fun getFirebaseAuthState() = callbackFlow  {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun signOut() = flow {
        try {
            emit(Loading)
            auth.signOut()
            oneTapClient.signOut().await()
            emit(Success(true))
        } catch (e: Exception) {
            emit(Error(e))
        }
    }

    override suspend fun revokeAccess() = flow {
        try {
            emit(Loading)
            auth.currentUser?.apply {
                db.collection(USERS_REF).document(uid).delete().await()
                delete().await()
                signInClient.revokeAccess().await()
                oneTapClient.signOut().await()
            }
            emit(Success(true))
        } catch (e: Exception) {
            emit(Error(e))
        }
    }
}