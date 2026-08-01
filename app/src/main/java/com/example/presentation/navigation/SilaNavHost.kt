package com.example.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.presentation.ui.screens.*
import com.example.presentation.viewmodel.*

@Composable
fun SilaNavHost(
    navController: NavHostController,
    silaViewModel: SilaViewModel,
    authViewModel: AuthViewModel,
    chatViewModel: ChatViewModel,
    callsViewModel: CallsViewModel,
    statusViewModel: StatusViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateNext = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onStartClick = {
                    navController.navigate(Screen.Onboarding.route)
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinishOnboarding = {
                    navController.navigate(Screen.AuthPhone.route)
                }
            )
        }

        composable(Screen.AuthPhone.route) {
            AuthPhoneScreen(
                authViewModel = authViewModel,
                onNavigateOtp = { fullPhone ->
                    navController.navigate(Screen.AuthOTP.createRoute(fullPhone))
                },
                onNavigateEmail = {
                    navController.navigate(Screen.AuthEmail.route)
                }
            )
        }

        composable(Screen.AuthEmail.route) {
            AuthEmailScreen(
                authViewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateOtp = { email ->
                    navController.navigate(Screen.AuthOTP.createRoute(email))
                }
            )
        }

        composable(
            route = Screen.AuthOTP.route,
            arguments = listOf(navArgument("phoneOrEmail") { type = NavType.StringType })
        ) { backStackEntry ->
            val target = backStackEntry.arguments?.getString("phoneOrEmail") ?: ""
            AuthOTPScreen(
                authViewModel = authViewModel,
                phoneOrEmailTarget = target,
                onNavigateBack = { navController.popBackStack() },
                onNavigateProfileSetup = {
                    navController.navigate(Screen.ProfileSetup.route)
                }
            )
        }

        composable(Screen.ProfileSetup.route) {
            ProfileSetupScreen(
                authViewModel = authViewModel,
                onNavigateHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                silaViewModel = silaViewModel,
                chatViewModel = chatViewModel,
                callsViewModel = callsViewModel,
                statusViewModel = statusViewModel,
                onNavigateChatDetail = { chatId ->
                    navController.navigate(Screen.ChatDetail.createRoute(chatId))
                },
                onNavigateCallScreen = { callId, name, isVideo ->
                    navController.navigate(Screen.CallScreen.createRoute(callId, name, isVideo))
                },
                onNavigateStatusViewer = { statusId ->
                    navController.navigate(Screen.StatusViewer.createRoute(statusId))
                },
                onNavigateCreateGroup = {
                    navController.navigate(Screen.CreateGroup.route)
                },
                onNavigateSearch = {
                    navController.navigate(Screen.SearchScreen.route)
                },
                onNavigateSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateContacts = {
                    navController.navigate(Screen.ContactsList.route)
                }
            )
        }

        composable(
            route = Screen.ChatDetail.route,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: "chat_1"
            ChatDetailScreen(
                chatId = chatId,
                chatViewModel = chatViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateCall = { callId, name, isVideo ->
                    callsViewModel.startCall(name, isVideo)
                    navController.navigate(Screen.CallScreen.createRoute(callId, name, isVideo))
                }
            )
        }

        composable(
            route = Screen.CallScreen.route,
            arguments = listOf(
                navArgument("callId") { type = NavType.StringType },
                navArgument("participantName") { type = NavType.StringType },
                navArgument("isVideo") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val callId = backStackEntry.arguments?.getString("callId") ?: ""
            val name = backStackEntry.arguments?.getString("participantName") ?: "مستخدم صلة"
            val isVideo = backStackEntry.arguments?.getBoolean("isVideo") ?: false
            CallScreen(
                callId = callId,
                participantName = name,
                isVideoCall = isVideo,
                onEndCall = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.StatusViewer.route,
            arguments = listOf(navArgument("statusId") { type = NavType.StringType })
        ) { backStackEntry ->
            val statusId = backStackEntry.arguments?.getString("statusId") ?: ""
            StatusViewerScreen(
                statusId = statusId,
                statusViewModel = statusViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.CreateGroup.route) {
            CreateGroupScreen(
                chatViewModel = chatViewModel,
                onNavigateBack = { navController.popBackStack() },
                onGroupCreated = { navController.popBackStack() }
            )
        }

        composable(Screen.ContactsList.route) {
            ContactsScreen(
                onNavigateBack = { navController.popBackStack() },
                onContactSelect = { chatId ->
                    navController.navigate(Screen.ChatDetail.createRoute(chatId))
                }
            )
        }

        composable(Screen.ProfileDetail.route) {
            ProfileScreen(
                silaViewModel = silaViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                silaViewModel = silaViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateProfile = { navController.navigate(Screen.ProfileDetail.route) },
                onNavigatePrivacy = { navController.navigate(Screen.PrivacySettings.route) },
                onNavigateBackup = { navController.navigate(Screen.ChatBackup.route) }
            )
        }

        composable(Screen.PrivacySettings.route) {
            PrivacySettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ChatBackup.route) {
            ChatBackupScreen(
                silaViewModel = silaViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.SearchScreen.route) {
            SearchScreen(
                chatViewModel = chatViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateChatDetail = { chatId ->
                    navController.navigate(Screen.ChatDetail.createRoute(chatId))
                }
            )
        }
    }
}
