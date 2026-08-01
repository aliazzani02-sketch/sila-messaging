package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datastore.UserPreferences
import com.example.data.repository.SilaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SilaViewModel(
    private val userPreferences: UserPreferences,
    private val silaRepository: SilaRepository
) : ViewModel() {

    val isDarkMode: StateFlow<Boolean> = userPreferences.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val userName: StateFlow<String> = userPreferences.userName
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "مستخدم صلة"
        )

    val userPhone: StateFlow<String> = userPreferences.userPhone
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    val userEmail: StateFlow<String> = userPreferences.userEmail
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    val userBio: StateFlow<String> = userPreferences.userBio
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "متصل عبر صلة ✨"
        )

    val notificationsEnabled: StateFlow<Boolean> = userPreferences.notificationsEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val lastBackupTime: StateFlow<String> = userPreferences.lastBackupTime
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "لم يتم إجراء نسخ احتياطي بعد"
        )

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.setDarkMode(enabled)
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.setNotificationsEnabled(enabled)
        }
    }

    fun performBackup() {
        viewModelScope.launch {
            val formatted = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
                .format(java.util.Date())
            userPreferences.updateLastBackupTime("تم التنسيق بتاريخ $formatted")
        }
    }

    fun updateProfile(name: String, bio: String) {
        viewModelScope.launch {
            userPreferences.saveUserProfile(
                name = name,
                phone = userPhone.value,
                email = userEmail.value,
                bio = bio
            )
        }
    }
}
