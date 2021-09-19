package com.diet.tracker.ui.auth

import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.diet.tracker.ui.DietActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.feature.firebase.auth.AuthCallback
import com.feature.firebase.auth.AuthManager
import com.feature.firebase.auth.di.FacebookPermissions
import com.feature.firebase.auth.domain.model.AuthUser
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.lang.IllegalStateException
import javax.inject.Inject

@AndroidEntryPoint
abstract class AuthActivity : AppCompatActivity(), AuthCallback {

    @Inject lateinit var authManager: AuthManager
    @Inject lateinit var googleSignInClient: GoogleSignInClient
    @Inject lateinit var facebookCallbackManager: CallbackManager

    @Inject
    @FacebookPermissions
    lateinit var facebookPermissions: ArrayList<String>

    private val googleLogin = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account.idToken?.let { token ->
                authManager.loginWithGoogle(token, this)
            }
        } catch (e: ApiException) {
            onLoginFailure(e)
        }
    }

    private val facebookCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {
            result?.accessToken?.let { accessToken ->
                authManager.loginWithFacebook(accessToken.token, this@AuthActivity)
            }
        }

        override fun onCancel() {
            onLoginFailure(IllegalStateException("User canceled the facebook login"))
        }

        override fun onError(error: FacebookException?) {
            onLoginFailure(error)
        }
    }

    protected fun signInWithGoogle() {
        googleLogin.launch(googleSignInClient.signInIntent)
    }

    protected fun signInWithFacebook() {
        with (LoginManager.getInstance()) {
            registerCallback(facebookCallbackManager, facebookCallback)
            logInWithReadPermissions(this@AuthActivity, facebookPermissions)
        }
    }

    protected fun signInAnonymous() {
        authManager.loginAnonymous(this)
    }

    override fun onLoginSuccess(user: AuthUser) {
        startActivity(Intent(this, DietActivity::class.java))
        finish()
    }

    override fun onLoginFailure(exc: Exception?) {
        Log.e("nt.dung", "Login failed -> ${exc?.message}")
        exc?.printStackTrace()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}