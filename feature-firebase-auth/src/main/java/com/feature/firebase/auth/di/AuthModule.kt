package com.feature.firebase.auth.di

import android.content.Context
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.feature.firebase.auth.AuthManager
import com.feature.firebase.auth.AuthManagerImpl
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @FacebookPermissions
    @Provides
    @Singleton
    fun provideFacebookPermissions(): ArrayList<String> = arrayListOf(
        "email",
        "public_profile"
    )

    @GoogleWebClientId
    @Provides
    @Singleton
    fun provideGoogleWebClientId() : String {
        return "608799868854-u2ju2gkr6l80srnnd3fe4175tnph3jg4.apps.googleusercontent.com"
    }

    @Provides
    @Singleton
    fun provideFacebookCallbackManager() : CallbackManager {
        return CallbackManager.Factory.create();
    }

    @Provides
    @Singleton
    fun provideGoogleSignInOption(@GoogleWebClientId webClientId: String) : GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
    }

    @Provides
    @Singleton
    fun provideGoogleSignInClient(gso: GoogleSignInOptions, @ApplicationContext context: Context) : GoogleSignInClient {
        return GoogleSignIn.getClient(context, gso)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthManager(firebaseAuth: FirebaseAuth): AuthManager = AuthManagerImpl(firebaseAuth)
}