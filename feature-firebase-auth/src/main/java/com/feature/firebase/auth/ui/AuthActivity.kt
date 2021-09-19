package com.feature.firebase.auth.ui

import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.feature.firebase.auth.AuthCallback
import com.feature.firebase.auth.AuthManager
import com.feature.firebase.auth.di.FacebookPermissions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalStateException
import javax.inject.Inject

@AndroidEntryPoint
abstract class AuthActivity : AppCompatActivity() {

    @Inject
    lateinit var authManager: AuthManager

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var facebookCallbackManager: CallbackManager

    @Inject
    @FacebookPermissions
    lateinit var facebookPermissions: ArrayList<String>

    private lateinit var authCallback: AuthCallback

    private val googleLogin =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { token ->
                    authManager.loginWithGoogle(token, authCallback)
                }
            } catch (e: ApiException) {
                authCallback.onLoginFailure(e)
            }
        }

    private val facebookCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {
            result?.accessToken?.let { accessToken ->
                authManager.loginWithFacebook(accessToken.token, authCallback)
            }
        }

        override fun onCancel() {
            authCallback.onLoginFailure(IllegalStateException("User canceled the facebook login"))
        }

        override fun onError(error: FacebookException?) {
            authCallback.onLoginFailure(error)
        }
    }

    fun registerAuthCallback(authCallback: AuthCallback) {
        this.authCallback = authCallback
    }

    fun registerSocialLoginView(
        btnGoogleLogin: View? = null,
        btnFacebookLogin: View? = null,
        btnAnonymousLogin: View? = null
    ) {
        btnGoogleLogin?.setOnClickListener { signInWithGoogle() }
        btnFacebookLogin?.setOnClickListener { signInWithFacebook() }
        btnAnonymousLogin?.setOnClickListener { signInAnonymous() }
    }

    fun registerEmailPasswordLoginView(
        btnEmailPasswdLogin: View? = null,
        funGetEmail: (() -> String?)? = null,
        funGetPasswd: (() -> String?)? = null
    ) {
        btnEmailPasswdLogin?.setOnClickListener {
            signInWithEmail(funGetEmail?.invoke(), funGetPasswd?.invoke())
        }
    }

    fun registerSignUpView(
        btnSignUp: View? = null,
        funGetEmail: (() -> String?)? = null,
        funGetPasswd: (() -> String?)? = null
    ) {
        btnSignUp?.setOnClickListener {
            registerWithEmail(funGetEmail?.invoke(), funGetPasswd?.invoke())
        }
    }

    private fun signInWithGoogle() {
        googleLogin.launch(googleSignInClient.signInIntent)
    }

    private fun signInWithFacebook() {
        with(LoginManager.getInstance()) {
            registerCallback(facebookCallbackManager, facebookCallback)
            logInWithReadPermissions(this@AuthActivity, facebookPermissions)
        }
    }

    private fun signInAnonymous() {
        authManager.loginAnonymous(authCallback)
    }

    private fun signInWithEmail(email: String?, password: String?) {
        authManager.loginWithEmailPassword(email, password, authCallback)
    }

    private fun registerWithEmail(email: String?, password: String?) {
        authManager.registerWithEmailPassword(email, password, authCallback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}