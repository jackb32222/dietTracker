package com.feature.firebase.auth

interface AuthManager {
    fun loginWithGoogle()
    fun loginWithFacebook()
    fun loginWithEmailPassword(email: String, passwd: String)
    fun loginAnonymous()
}