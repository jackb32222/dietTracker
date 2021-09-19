package com.feature.firebase.auth

import com.feature.firebase.auth.domain.model.AuthUser
import java.lang.Exception

interface AuthManager {
    fun registerWithEmailPassword(email: String?, passwd: String?, callback: AuthCallback)
    fun loginWithGoogle(idToken: String, callback: AuthCallback)
    fun loginWithFacebook(accessToken: String, callback: AuthCallback)
    fun loginWithEmailPassword(email: String?, passwd: String?, callback: AuthCallback)
    fun loginAnonymous(callback: AuthCallback)
}

