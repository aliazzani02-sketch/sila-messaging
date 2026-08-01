package com.example.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sila_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_PHONE = stringPreferencesKey("user_phone")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_AVATAR = stringPreferencesKey("user_avatar")
        val USER_BIO = stringPreferencesKey("user_bio")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val LAST_BACKUP_TIME = stringPreferencesKey("last_backup_time")
        val PRIVACY_LAST_SEEN = stringPreferencesKey("privacy_last_seen")
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[IS_DARK_MODE] ?: true // Default dark theme for Sila
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[IS_LOGGED_IN] ?: false
    }

    val userName: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[USER_NAME] ?: "مستخدم صلة"
    }

    val userPhone: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[USER_PHONE] ?: "+966 50 123 4567"
    }

    val userEmail: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[USER_EMAIL] ?: ""
    }

    val userAvatar: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[USER_AVATAR] ?: ""
    }

    val userBio: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[USER_BIO] ?: "متصل عبر صلة ✨"
    }

    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[NOTIFICATIONS_ENABLED] ?: true
    }

    val lastBackupTime: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[LAST_BACKUP_TIME] ?: "لم يتم إجراء نسخ احتياطي بعد"
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_DARK_MODE] = enabled
        }
    }

    suspend fun saveUserProfile(
        name: String,
        phone: String,
        email: String = "",
        avatar: String = "",
        bio: String = "متصل عبر صلة ✨"
    ) {
        context.dataStore.edit { prefs ->
            prefs[USER_NAME] = name
            prefs[USER_PHONE] = phone
            prefs[USER_EMAIL] = email
            prefs[USER_AVATAR] = avatar
            prefs[USER_BIO] = bio
            prefs[IS_LOGGED_IN] = true
        }
    }

    suspend fun linkEmail(email: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_EMAIL] = email
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun updateLastBackupTime(formattedTime: String) {
        context.dataStore.edit { prefs ->
            prefs[LAST_BACKUP_TIME] = formattedTime
        }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = false
        }
    }
}
