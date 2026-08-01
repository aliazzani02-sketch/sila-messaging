package com.example.data.repository

import com.example.data.datastore.UserPreferences
import com.example.domain.model.CountryCode
import com.example.domain.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class AuthRepository(
    private val userPreferences: UserPreferences
) {
    val isLoggedIn: Flow<Boolean> = userPreferences.isLoggedIn
    val userName: Flow<String> = userPreferences.userName
    val userPhone: Flow<String> = userPreferences.userPhone
    val userEmail: Flow<String> = userPreferences.userEmail
    val userAvatar: Flow<String> = userPreferences.userAvatar
    val userBio: Flow<String> = userPreferences.userBio

    val countryCodes = listOf(
        CountryCode("المملكة العربية السعودية", "+966", "🇸🇦"),
        CountryCode("الإمارات العربية المتحدة", "+971", "🇦🇪"),
        CountryCode("مصر", "+20", "🇪🇬"),
        CountryCode("الكويت", "+965", "🇰🇼"),
        CountryCode("قطر", "+974", "🇶🇦"),
        CountryCode("البحرين", "+973", "🇧🇭"),
        CountryCode("سلطنة عمان", "+968", "🇴🇲"),
        CountryCode("الأردن", "+962", "🇯🇴"),
        CountryCode("العراق", "+964", "🇮🇶"),
        CountryCode("المغرب", "+212", "🇲🇦"),
        CountryCode("تونس", "+216", "🇹🇳"),
        CountryCode("الجزائر", "+213", "🇩🇿"),
        CountryCode("لبنان", "+961", "🇱🇧"),
        CountryCode("فلسطين", "+970", "🇵🇸")
    )

    suspend fun requestPhoneOtp(countryCode: String, phoneNumber: String): Boolean {
        delay(1000) // Simulated network verification
        return phoneNumber.length >= 7
    }

    suspend fun verifyOtp(otpCode: String): Boolean {
        delay(1200) // Simulated OTP verification
        return otpCode == "123456" || otpCode.length == 6
    }

    suspend fun requestEmailLink(email: String): Boolean {
        delay(1000)
        return email.contains("@") && email.contains(".")
    }

    suspend fun completeProfileRegistration(
        name: String,
        phone: String,
        email: String = "",
        avatarUrl: String = "",
        bio: String = "متصل عبر صلة ✨"
    ) {
        userPreferences.saveUserProfile(
            name = name,
            phone = phone,
            email = email,
            avatar = avatarUrl,
            bio = bio
        )
    }

    suspend fun linkEmailAccount(email: String) {
        userPreferences.linkEmail(email)
    }

    suspend fun logout() {
        userPreferences.logout()
    }
}
