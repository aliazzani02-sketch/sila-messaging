package com.example.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.ui.components.AvatarView
import com.example.presentation.viewmodel.SilaViewModel
import com.example.ui.theme.SilaPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    silaViewModel: SilaViewModel,
    onNavigateBack: () -> Unit,
    onNavigateProfile: () -> Unit,
    onNavigatePrivacy: () -> Unit,
    onNavigateBackup: () -> Unit
) {
    val userName by silaViewModel.userName.collectAsState()
    val userPhone by silaViewModel.userPhone.collectAsState()
    val userBio by silaViewModel.userBio.collectAsState()
    val isDarkMode by silaViewModel.isDarkMode.collectAsState()
    val notificationsEnabled by silaViewModel.notificationsEnabled.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("إعدادات صلة") },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Header Profile Card
            Card(
                onClick = onNavigateProfile,
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AvatarView(name = userName, size = 60.dp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = userBio,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = userPhone,
                            fontSize = 12.sp,
                            color = SilaPrimary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "تعديل",
                        tint = SilaPrimary
                    )
                }
            }

            // Options List
            SettingsSectionTitle("إعدادات الحساب والخصوصية")
            SettingsItem(
                title = "الخصوصية والأمان",
                subtitle = "التشفير، قفل الحساب، الظهور والقصص",
                icon = Icons.Outlined.Lock,
                onClick = onNavigatePrivacy
            )
            SettingsItem(
                title = "النسخ الاحتياطي للمحادثات",
                subtitle = "استعادة السجلات، السحابة والتخزين المحلي",
                icon = Icons.Outlined.CloudUpload,
                onClick = onNavigateBackup
            )

            SettingsSectionTitle("التفضيلات والمظهر")
            SettingsToggleItem(
                title = "الوضع الليلي (Dark Theme)",
                subtitle = "تطبيق المظهر المظلم الفاخر",
                icon = Icons.Outlined.DarkMode,
                checked = isDarkMode,
                onCheckedChange = { silaViewModel.toggleDarkMode(it) }
            )
            SettingsToggleItem(
                title = "الإشعارات والتنبيهات",
                subtitle = "أصوات الرسائل والمكالمات الواردة",
                icon = Icons.Outlined.Notifications,
                checked = notificationsEnabled,
                onCheckedChange = { silaViewModel.toggleNotifications(it) }
            )

            SettingsSectionTitle("الدعم والمعلومات")
            SettingsItem(
                title = "حول تطبيق صلة (Sila)",
                subtitle = "الإصدار 1.0.0 • تشفير تام لطرفي الاتصال",
                icon = Icons.Outlined.Info,
                onClick = {}
            )
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = SilaPrimary,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 4.dp)
    )
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SilaPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SettingsToggleItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = SilaPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = SilaPrimary)
            )
        }
    }
}
