package com.diet.tracker.di

import android.content.Context
import com.diet.tracker.datasource.local.AppDataStore
import com.diet.tracker.notification.DietAlarmManager
import com.diet.tracker.notification.DietNotificationManager
import com.diet.tracker.repository.DietRepo
import com.diet.tracker.repository.DietRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DietModule {

    @Provides
    @Singleton
    fun provideAlarmManager(@ApplicationContext context: Context) = DietAlarmManager(context)

    @Provides
    @Singleton
    fun provideAppDatastore(@ApplicationContext context: Context) = AppDataStore(
        coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        context = context
    )

    @Provides
    @Singleton
    fun provideRepository(dataStore: AppDataStore): DietRepo = DietRepoImpl(dataStore)

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context) = DietNotificationManager(context)
}