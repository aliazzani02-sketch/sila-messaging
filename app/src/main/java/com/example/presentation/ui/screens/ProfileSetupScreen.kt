package com.example.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.viewmodel.AuthViewModel
import com.example.ui.theme.SilaPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    authViewModel: AuthViewModel,
    onNavigateHome: () -> Unit
) {
    val userName by authViewModel.userName.collectAsState()
    val userBio by authViewModel.userBio.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("إنشاء الملف الشخصي") }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "صمم حضورك في صلة ✨",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "أضف اسمك الشخصي والحالة لتتعرف عليك جهات اتصالك بسرعة.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Avatar Picker Target
                Box(
                    modifier = Modifier.size(110.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Surface(
                        shape = CircleShape,
                        color = SilaPrimary.copy(alpha = 0.15f),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = SilaPrimary,
                                modifier = Modifier.size(54.dp)
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = SilaPrimary,
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { /* Select Image */ }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "اختيار صورة",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = userName,
                    onValueChange = { authViewModel.onNameChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("الاسم الكريم") },
                    placeholder = { Text("مثال: عبد الله أحمد") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = userBio,
                    onValueChange = { authViewModel.onBioChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("نبذة / الحالة") },
                    placeholder = { Text("متصل عبر صلة ✨") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }

            Button(
                onClick = {
                    authViewModel.finishProfileSetup {
                        onNavigateHome()
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SilaPrimary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "حفظ ودخول التطبيق",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
