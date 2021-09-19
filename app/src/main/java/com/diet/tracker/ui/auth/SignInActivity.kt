package com.diet.tracker.ui.auth

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.diet.tracker.R
import com.diet.tracker.databinding.ActivitySiginInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AuthActivity() {

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
        binding.btnSignIn.setOnClickListener { signIn() }
        binding.incSocialAuth.btnGoogle.setOnClickListener { signInWithGoogle() }
        binding.incSocialAuth.btnFacebook.setOnClickListener { signInWithFacebook() }
        binding.incSocialAuth.btnAnonymous.setOnClickListener { signInAnonymous() }
    }

    private fun signIn() {
        authManager.loginWithEmailPassword(email, password, this)
    }
}