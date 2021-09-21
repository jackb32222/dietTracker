package com.feature.firebase.auth

import com.feature.firebase.auth.domain.model.AuthUser
import com.google.firebase.auth.FirebaseUser
import java.lang.Exception

interface AuthCallback {
    fun onLoginSuccess(user: FirebaseUser) {}
    fun onLoginFailure(exc: Exception?) {}
    fun onRegisterSuccess(user: FirebaseUser) {}
    fun onRegisterFailure(exc: Exception?) {}
}