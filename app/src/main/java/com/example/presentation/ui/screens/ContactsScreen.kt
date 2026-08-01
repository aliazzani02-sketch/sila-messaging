package com.example.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.ui.components.AvatarView
import com.example.ui.theme.SilaPrimary

data class ContactItemData(val id: String, val name: String, val phone: String, val bio: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    onNavigateBack: () -> Unit,
    onContactSelect: (chatId: String) -> Unit
) {
    val contacts = listOf(
        ContactItemData("chat_1", "أحمد المحمدي", "+966 50 111 2222", "متصل عبر صلة ✨"),
        ContactItemData("chat_4", "نورة خالد", "+966 55 333 4444", "سبحان الله وبحمده"),
        ContactItemData("contact_new_1", "عبدالله العتيبي", "+966 54 555 6666", "مشغول حالياً"),
        ContactItemData("contact_new_2", "سارة علي", "+966 56 777 8888", "أستغفر الله العظيم"),
        ContactItemData("contact_new_3", "فيصل الدوسري", "+966 59 999 0000", "صلة تواصل رائع")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("جهات الاتصال (${contacts.size})") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "رجوع")
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(contacts) { contact ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onContactSelect(contact.id) }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AvatarView(name = contact.name, size = 48.dp)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = contact.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = contact.bio,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = { onContactSelect(contact.id) }) {
                        Icon(imageVector = Icons.Default.Chat, contentDescription = "بدء محادثة", tint = SilaPrimary)
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            }
        }
    }
}
