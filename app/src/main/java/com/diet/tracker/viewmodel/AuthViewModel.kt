package com.diet.tracker.viewmodel

import androidx.lifecycle.ViewModel
import com.feature.firebase.auth.AuthManager
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authManager: AuthManager) : ViewModel() {

    fun getUserId() = authManager.getUserId()
}