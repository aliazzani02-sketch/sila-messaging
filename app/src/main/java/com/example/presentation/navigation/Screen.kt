package com.example.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Onboarding : Screen("onboarding")
    object AuthPhone : Screen("auth_phone")
    object AuthEmail : Screen("auth_email")
    object AuthOTP : Screen("auth_otp/{phoneOrEmail}") {
        fun createRoute(phoneOrEmail: String) = "auth_otp/$phoneOrEmail"
    }
    object ProfileSetup : Screen("profile_setup")
    object Home : Screen("home")
    object ChatDetail : Screen("chat_detail/{chatId}") {
        fun createRoute(chatId: String) = "chat_detail/$chatId"
    }
    object CallScreen : Screen("call_screen/{callId}/{participantName}/{isVideo}") {
        fun createRoute(callId: String, participantName: String, isVideo: Boolean) =
            "call_screen/$callId/$participantName/$isVideo"
    }
    object StatusViewer : Screen("status_viewer/{statusId}") {
        fun createRoute(statusId: String) = "status_viewer/$statusId"
    }
    object CreateGroup : Screen("create_group")
    object CommunityDetail : Screen("community_detail/{communityId}") {
        fun createRoute(communityId: String) = "community_detail/$communityId"
    }
    object ContactsList : Screen("contacts_list")
    object ProfileDetail : Screen("profile_detail")
    object Settings : Screen("settings")
    object PrivacySettings : Screen("privacy_settings")
    object ChatBackup : Screen("chat_backup")
    object SearchScreen : Screen("search_screen")
    object MediaViewer : Screen("media_viewer")
}
