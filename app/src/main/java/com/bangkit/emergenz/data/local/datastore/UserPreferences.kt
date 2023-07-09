package com.bangkit.emergenz.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    fun getSession(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[SESSION_STATE] ?: false
        }
    }

    fun getEmail(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[EMAIL] ?: ""
        }
    }

    fun getName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[NAME] ?: ""
        }
    }

    suspend fun saveToken(Token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = Token
        }
    }

    suspend fun setEmail(Email: String){
        dataStore.edit { preferences ->
            preferences[EMAIL] = Email
        }
    }

    suspend fun setSession(Session: Boolean) {
        dataStore.edit { preferences ->
            preferences[SESSION_STATE] = Session
        }
    }

    suspend fun setUser(Username: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME] = Username
        }
    }

    suspend fun saveName(Name: String) {
        dataStore.edit { preferences ->
            preferences[NAME] = Name
        }
    }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("bearer_token")
        private val SESSION_STATE = booleanPreferencesKey("on_session")
        private val USERNAME = stringPreferencesKey("username")
        private val EMAIL = stringPreferencesKey("email")
        private val NAME = stringPreferencesKey("name")

        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}