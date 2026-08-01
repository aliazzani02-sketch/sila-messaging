package com.example.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.ui.components.AvatarView
import com.example.presentation.viewmodel.SilaViewModel
import com.example.ui.theme.SilaPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    silaViewModel: SilaViewModel,
    onNavigateBack: () -> Unit
) {
    val userName by silaViewModel.userName.collectAsState()
    val userPhone by silaViewModel.userPhone.collectAsState()
    val userEmail by silaViewModel.userEmail.collectAsState()
    val userBio by silaViewModel.userBio.collectAsState()

    var nameInput by remember { mutableStateOf(userName) }
    var bioInput by remember { mutableStateOf(userBio) }
    var isSaved by remember { mutableStateOf(false) }

    LaunchedEffect(userName, userBio) {
        nameInput = userName
        bioInput = userBio
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("الملف الشخصي") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "رجوع")
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(110.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    AvatarView(name = userName, size = 110.dp)
                    Surface(
                        shape = CircleShape,
                        color = SilaPrimary,
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { /* Select image */ }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "تغيير الصورة",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("الاسم") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = bioInput,
                    onValueChange = { bioInput = it },
                    label = { Text("نبذة / الحالة الشخصية") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = userPhone,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("رقم الهاتف (موثق)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = userEmail.ifBlank { "لم يربط بريد إلكتروني بعد" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("البريد الإلكتروني المرتبط") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                if (isSaved) {
                    Text(
                        text = "تم حفظ التغييرات بنجاح ✨",
                        color = SilaPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }

            Button(
                onClick = {
                    silaViewModel.updateProfile(nameInput, bioInput)
                    isSaved = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SilaPrimary)
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "حفظ التغييرات",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
