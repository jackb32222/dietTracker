package com.diet.tracker.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.diet.tracker.ui.auth.SignInActivity
import com.feature.firebase.auth.AuthManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {
    @Inject lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (authManager.isLoggedIn()) {
            startActivity(Intent(this, DietActivity::class.java))
        } else {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

}