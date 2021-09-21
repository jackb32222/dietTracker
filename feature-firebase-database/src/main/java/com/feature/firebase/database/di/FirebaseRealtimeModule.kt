package com.feature.firebase.database.di

import com.feature.firebase.database.RealtimeRepo
import com.feature.firebase.database.RealtimeRepoImpl
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseRealtimeModule {

    @Provides
    @Singleton
    fun provideRealtimeDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideRTRepo(database: FirebaseDatabase) : RealtimeRepo = RealtimeRepoImpl(database)
}