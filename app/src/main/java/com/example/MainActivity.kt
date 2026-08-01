package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.data.datastore.UserPreferences
import com.example.data.local.AppDatabase
import com.example.data.repository.AuthRepository
import com.example.data.repository.SilaRepository
import com.example.presentation.navigation.SilaNavHost
import com.example.presentation.viewmodel.*
import com.example.ui.theme.SilaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Local Data Layer & Datastore initialization
        val database = AppDatabase.getInstance(applicationContext)
        val userPreferences = UserPreferences(applicationContext)

        val silaRepository = SilaRepository(appDatabase = database)

        val authRepository = AuthRepository(userPreferences)

        setContent {
            val silaViewModel = remember { SilaViewModel(userPreferences, silaRepository) }
            val authViewModel = remember { AuthViewModel(authRepository) }
            val chatViewModel = remember { ChatViewModel(silaRepository) }
            val callsViewModel = remember { CallsViewModel(silaRepository) }
            val statusViewModel = remember { StatusViewModel(silaRepository) }

            val isDarkMode by silaViewModel.isDarkMode.collectAsState()

            SilaTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    SilaNavHost(
                        navController = navController,
                        silaViewModel = silaViewModel,
                        authViewModel = authViewModel,
                        chatViewModel = chatViewModel,
                        callsViewModel = callsViewModel,
                        statusViewModel = statusViewModel
                    )
                }
            }
        }
    }
}
