package com.example.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SilaPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacySettingsScreen(
    onNavigateBack: () -> Unit
) {
    var lastSeenPrivacy by remember { mutableStateOf("الجميع") }
    var profilePhotoPrivacy by remember { mutableStateOf("جهات اتصالي") }
    var isBiometricEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("الخصوصية والأمان") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SilaPrimary.copy(alpha = 0.1f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Security,
                        contentDescription = null,
                        tint = SilaPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "تشفير تام للطرفين (End-to-End)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "مفاتيح التشفير مخزنة على جهازك محلياً ولا يستطيع خادم صلة الاطلاع عليها.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Text(
                text = "من يمكنه رؤية معلوماتك الشخصية؟",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = SilaPrimary,
                modifier = Modifier.padding(start = 4.dp)
            )

            Card(shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "آخر ظهور والمتصل الآن", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                        TextButton(onClick = {
                            lastSeenPrivacy = if (lastSeenPrivacy == "الجميع") "جهات اتصالي" else "الجميع"
                        }) {
                            Text(text = lastSeenPrivacy, color = SilaPrimary)
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "الصورة الشخصية", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                        TextButton(onClick = {
                            profilePhotoPrivacy = if (profilePhotoPrivacy == "جهات اتصالي") "الجميع" else "جهات اتصالي"
                        }) {
                            Text(text = profilePhotoPrivacy, color = SilaPrimary)
                        }
                    }
                }
            }

            Text(
                text = "أمان التطبيق",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = SilaPrimary,
                modifier = Modifier.padding(start = 4.dp)
            )

            Card(shape = RoundedCornerShape(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Outlined.Lock, contentDescription = null, tint = SilaPrimary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "قفل التطبيق ببصمة الاصبع", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(text = "طلب البصمة عند فتح التطبيق", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Switch(checked = isBiometricEnabled, onCheckedChange = { isBiometricEnabled = it })
                }
            }
        }
    }
}
