package com.diet.tracker.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.diet.tracker.R
import com.diet.tracker.databinding.ActivityRegisterBinding
import com.diet.tracker.ui.DietActivity
import com.feature.firebase.auth.AuthCallback
import com.feature.firebase.auth.domain.model.AuthUser
import com.feature.firebase.auth.ui.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class RegisterActivity : AuthActivity(), AuthCallback {

    private lateinit var binding: ActivityRegisterBinding
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        initListeners()
    }

    private fun initListeners() {
        registerAuthCallback(this)

        registerSignUpView(
            btnSignUp = binding.btnRegister,
            funGetEmail = { email },
            funGetPasswd = { password }
        )
    }

    override fun onRegisterSuccess(user: AuthUser) {
        startActivity(Intent(this, DietActivity::class.java))
        finish()
    }

    override fun onRegisterFailure(exc: Exception?) {
        Log.e("nt.dung", "Login failed -> ${exc?.message}")
        exc?.printStackTrace()
    }

}