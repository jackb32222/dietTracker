package com.diet.tracker.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class AppDataStore(
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = context.packageName)

    fun putString(key: String, value: String) {
        coroutineScope.launch(dispatcher) {
            context.dataStore.edit { preferences ->
                preferences[stringPreferencesKey(key)] = value
            }
        }
    }

    fun putStringSet(key: String, values: Set<String>) {
        coroutineScope.launch(dispatcher) {
            context.dataStore.edit { preferences ->
                preferences[stringSetPreferencesKey(key)] = values
            }
        }
    }

    fun putInt(key: String, value: Int) {
        coroutineScope.launch(dispatcher) {
            context.dataStore.edit { preferences ->
                preferences[intPreferencesKey(key)] = value
            }
        }
    }

    fun putLong(key: String, value: Long) {
        coroutineScope.launch(dispatcher) {
            context.dataStore.edit { preferences ->
                preferences[longPreferencesKey(key)] = value
            }
        }
    }

    fun putFloat(key: String, value: Float) {
        coroutineScope.launch(dispatcher) {
            context.dataStore.edit { preferences ->
                preferences[floatPreferencesKey(key)] = value
            }
        }
    }

    fun putBoolean(key: String, value: Boolean) {
        coroutineScope.launch(dispatcher) {
            context.dataStore.edit { preferences ->
                preferences[booleanPreferencesKey(key)] = value
            }
        }
    }

    fun getFlowString(key: String, defValue: String): Flow<String> {
       return context.dataStore.data.map { preferences ->
           preferences[stringPreferencesKey(key)] ?: defValue
       }
    }

    fun getFlowStringSet(key: String, defValues: Set<String>): Flow<Set<String>> {
        return context.dataStore.data.map { preferences ->
            preferences[stringSetPreferencesKey(key)] ?: defValues
        }
    }

    fun getFlowInt(key: String, defValue: Int): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[intPreferencesKey(key)] ?: defValue
        }
    }

    fun getFlowLong(key: String, defValue: Long): Flow<Long> {
        return context.dataStore.data.map { preferences ->
            preferences[longPreferencesKey(key)] ?: defValue
        }
    }

    fun getFlowFloat(key: String, defValue: Float): Flow<Float> {
        return context.dataStore.data.map { preferences ->
            preferences[floatPreferencesKey(key)] ?: defValue
        }
    }

    fun getFlowBoolean(key: String, defValue: Boolean): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(key)] ?: defValue
        }
    }

    fun getString(key: String, defValue: String): String {
        return runBlocking {
            getFlowString(key, defValue).first()
        }
    }

    fun getStringSet(key: String, defValues: Set<String>): Set<String> {
        return runBlocking {
            getFlowStringSet(key, defValues).first()
        }
    }

    fun getInt(key: String, defValue: Int): Int {
        return runBlocking {
            getFlowInt(key, defValue).first()
        }
    }

    fun getLong(key: String, defValue: Long): Long {
        return runBlocking {
            getFlowLong(key, defValue).first()
        }
    }

    fun getFloat(key: String, defValue: Float): Float {
        return runBlocking {
            getFlowFloat(key, defValue).first()
        }
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return runBlocking {
            getFlowBoolean(key, defValue).first()
        }
    }
}