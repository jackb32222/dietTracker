package com.diet.tracker.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.diet.tracker.R
import com.diet.tracker.databinding.ActivitySiginInBinding
import com.diet.tracker.ui.DietActivity
import com.feature.firebase.auth.AuthCallback
import com.feature.firebase.auth.domain.model.AuthUser
import com.feature.firebase.auth.ui.AuthActivity
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class SignInActivity : AuthActivity(), AuthCallback {

    private lateinit var binding: ActivitySiginInBinding

    private var email: String?
        get() = binding.inputEmail.editText?.text?.toString()
        set(value) {
            binding.inputEmail.editText?.setText(value)
        }

    private var password: String?
        get() = binding.inputPasswd.editText?.text?.toString()
        set(value) {
            binding.inputPasswd.editText?.setText(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sigin_in)

        initListeners()
    }

    private fun initListeners() {
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        registerAuthCallback(this)

        registerSocialLoginView(
            btnGoogleLogin = binding.incSocialAuth.btnGoogle,
            btnFacebookLogin = binding.incSocialAuth.btnFacebook,
            btnAnonymousLogin = binding.incSocialAuth.btnAnonymous
        )

        registerEmailPasswordLoginView(
            btnEmailPasswdLogin = binding.btnSignIn,
            funGetEmail = { email },
            funGetPasswd = { password }
        )
    }

    override fun onLoginSuccess(user: FirebaseUser) {
        startActivity(Intent(this, DietActivity::class.java))
        finish()
    }

    override fun onLoginFailure(exc: Exception?) {
        Log.e("nt.dung", "Login failed -> ${exc?.message}")
        exc?.printStackTrace()
    }
}