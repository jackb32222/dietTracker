package com.feature.firebase.auth

import com.feature.firebase.auth.domain.model.AuthUser
import java.lang.Exception

interface AuthCallback {
    fun onLoginSuccess(user: AuthUser) {}
    fun onLoginFailure(exc: Exception?) {}
    fun onRegisterSuccess() {}
    fun onRegisterFailure() {}
}