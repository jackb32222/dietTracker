package com.diet.tracker.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.diet.tracker.R
import com.diet.tracker.databinding.ActivityRegisterBinding
import com.diet.tracker.ui.DietActivity
import com.feature.firebase.auth.AuthCallback
import com.feature.firebase.auth.domain.model.AuthUser
import com.feature.firebase.auth.ui.AuthActivity
import com.google.firebase.auth.FirebaseUser
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

    private var confirmedPassword: String?
        get() = binding.inputConfirmPasswd.editText?.text?.toString()
        set(value) {
            binding.inputConfirmPasswd.editText?.setText(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        initListeners()
        checkContinue()
    }

    private fun initListeners() {
        binding.inputEmail.editText?.doAfterTextChanged { checkContinue() }
        binding.inputPasswd.editText?.doAfterTextChanged { checkContinue() }
        binding.inputConfirmPasswd.editText?.doAfterTextChanged { checkContinue() }

        registerAuthCallback(this)

        registerSignUpView(
            btnSignUp = binding.btnRegister,
            funGetEmail = { email },
            funGetPasswd = { password }
        )
    }

    override fun onRegisterSuccess(user: FirebaseUser) {
        startActivity(Intent(this, DietActivity::class.java))
        finish()
    }

    override fun onRegisterFailure(exc: Exception?) {
        Toast.makeText(this, "Register failed", Toast.LENGTH_SHORT).show()
        Log.e("nt.dung", "Login failed -> ${exc?.message}")
        exc?.printStackTrace()
    }

    private fun checkContinue() {
        binding.btnRegister.isEnabled = !email.isNullOrEmpty()
                && !password.isNullOrEmpty()
                && !confirmedPassword.isNullOrEmpty()
                && (password == confirmedPassword)
    }

}