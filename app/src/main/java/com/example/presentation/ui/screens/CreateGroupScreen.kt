package com.example.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.presentation.viewmodel.ChatViewModel
import com.example.ui.theme.SilaPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    chatViewModel: ChatViewModel,
    onNavigateBack: () -> Unit,
    onGroupCreated: () -> Unit
) {
    var groupName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val sampleContacts = remember {
        listOf("أحمد المحمدي", "نورة خالد", "عبدالله العتيبي", "سارة علي", "فيصل الدوسري", "مريم الخالد")
    }

    val selectedContacts = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("إنشاء مجموعة جديدة") },
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
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                OutlinedTextField(
                    value = groupName,
                    onValueChange = { groupName = it },
                    label = { Text("اسم المجموعة") },
                    placeholder = { Text("مثال: مشروع تخرج صلة 🎓") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("وصف المجموعة (اختياري)") },
                    placeholder = { Text("المجال والأهداف العامة...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "إضافة أعضاء (${selectedContacts.size} محددين)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(modifier = Modifier.height(300.dp)) {
                    items(sampleContacts) { contact ->
                        val isSelected = selectedContacts.contains(contact)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isSelected) selectedContacts.remove(contact)
                                    else selectedContacts.add(contact)
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AvatarView(name = contact, size = 44.dp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = contact,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = {
                                    if (it) selectedContacts.add(contact)
                                    else selectedContacts.remove(contact)
                                },
                                colors = CheckboxDefaults.colors(checkedColor = SilaPrimary)
                            )
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    }
                }
            }

            Button(
                onClick = {
                    if (groupName.isNotBlank()) {
                        chatViewModel.createGroup(
                            name = groupName,
                            description = description,
                            selectedContacts = selectedContacts.toList(),
                            onSuccess = onGroupCreated
                        )
                    }
                },
                enabled = groupName.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SilaPrimary)
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "تأكيد وإنشاء المجموعة",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
