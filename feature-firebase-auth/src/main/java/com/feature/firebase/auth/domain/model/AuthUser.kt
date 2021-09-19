package com.feature.firebase.auth.domain.model

import com.google.firebase.auth.FirebaseUser

data class AuthUser(
    val name: String?
)

fun FirebaseUser.toAuthUser() : AuthUser {
    return AuthUser(
        name = this.displayName
    )
}