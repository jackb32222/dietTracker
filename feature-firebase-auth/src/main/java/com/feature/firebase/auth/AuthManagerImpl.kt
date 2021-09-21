package com.feature.firebase.auth

import com.feature.firebase.auth.domain.model.toAuthUser
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.IllegalArgumentException

class AuthManagerImpl(private val firebaseAuth: FirebaseAuth) : AuthManager {

    override fun getUserId(): String {
        return firebaseAuth.currentUser?.uid.orEmpty()
    }

    override fun registerWithEmailPassword(email: String?, passwd: String?, callback: AuthCallback) {
        if (!email.isNullOrEmpty() && !passwd.isNullOrEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, passwd)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        firebaseAuth.currentUser?.let { firebaseUser ->
                            callback.onRegisterSuccess(firebaseUser)
                        }
                    } else {
                        callback.onRegisterFailure(it.exception)
                    }
                }
                .addOnFailureListener {
                    callback.onRegisterFailure(it)
                }
        } else {
            callback.onRegisterFailure(IllegalArgumentException("Email & Password must not empty"))
        }
    }

    override fun loginWithGoogle(idToken: String, callback: AuthCallback) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseAuth.currentUser?.let { firebaseUser ->
                        callback.onLoginSuccess(firebaseUser)
                    }
                } else {
                    callback.onLoginFailure(it.exception)
                }
            }
            .addOnFailureListener {
                callback.onLoginFailure(it)
            }
    }

    override fun loginWithFacebook(accessToken: String, callback: AuthCallback) {
        val credential = FacebookAuthProvider.getCredential(accessToken)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseAuth.currentUser?.let { firebaseUser ->
                        callback.onLoginSuccess(firebaseUser)
                    }
                } else {
                    callback.onLoginFailure(it.exception)
                }
            }
            .addOnFailureListener {
                callback.onLoginFailure(it)
            }
    }

    override fun loginWithEmailPassword(email: String?, passwd: String?, callback: AuthCallback) {
        if (!email.isNullOrEmpty() && !passwd.isNullOrEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, passwd)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        firebaseAuth.currentUser?.let { firebaseUser ->
                            callback.onLoginSuccess(firebaseUser)
                        }
                    } else {
                        callback.onLoginFailure(it.exception)
                    }
                }
                .addOnFailureListener {
                    callback.onLoginFailure(it)
                }
        } else {
            callback.onLoginFailure(IllegalArgumentException("Email & Password must not empty"))
        }
    }

    override fun loginAnonymous(callback: AuthCallback) {
        firebaseAuth.signInAnonymously()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseAuth.currentUser?.let { firebaseUser ->
                        callback.onLoginSuccess(firebaseUser)
                    }
                } else {
                    callback.onLoginFailure(it.exception)
                }
            }
            .addOnFailureListener {
                callback.onLoginFailure(it)
            }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun isLoggedIn() = firebaseAuth.currentUser != null
}