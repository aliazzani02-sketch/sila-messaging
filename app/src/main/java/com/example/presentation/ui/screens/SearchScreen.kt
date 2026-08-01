package com.example.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.viewmodel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    chatViewModel: ChatViewModel,
    onNavigateBack: () -> Unit,
    onNavigateChatDetail: (chatId: String) -> Unit
) {
    val searchQuery by chatViewModel.searchQuery.collectAsState()
    val filteredChats by chatViewModel.filteredChats.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { chatViewModel.onSearchQueryChanged(it) },
                        placeholder = { Text("بحث في المحادثات والرسائل...") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 48.dp),
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true
                    )
                },
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
            items(filteredChats) { chat ->
                ChatItemRow(chat = chat, onClick = { onNavigateChatDetail(chat.id) })
            }
        }
    }
}
