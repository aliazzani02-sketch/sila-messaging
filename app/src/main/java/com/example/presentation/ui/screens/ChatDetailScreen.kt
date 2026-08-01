package com.example.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.MessageType
import com.example.presentation.ui.components.AvatarView
import com.example.presentation.ui.components.ChatMessageItem
import com.example.presentation.ui.components.VoiceNoteRecorderBar
import com.example.presentation.viewmodel.ChatViewModel
import com.example.ui.theme.SilaPrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    chatId: String,
    chatViewModel: ChatViewModel,
    onNavigateBack: () -> Unit,
    onNavigateCall: (callId: String, name: String, isVideo: Boolean) -> Unit
) {
    val chats by chatViewModel.chats.collectAsState()
    val chat = remember(chats, chatId) {
        chats.find { it.id == chatId }
    }

    val messages by chatViewModel.getMessages(chatId).collectAsState(initial = emptyList())
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    var inputText by remember { mutableStateOf("") }
    var isRecordingVoice by remember { mutableStateOf(false) }
    var showAttachmentSheet by remember { mutableStateOf(false) }

    val chatTitle = chat?.name ?: "محادثات صلة"

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AvatarView(
                            name = chatTitle,
                            avatarUrl = chat?.avatarUrl ?: "",
                            size = 40.dp,
                            isOnline = chat?.isOnline ?: true
                        )
                        Column {
                            Text(
                                text = chatTitle,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            Text(
                                text = if (chat?.isOnline == true) "متصل الآن 🟢" else "آخر ظهور حديثاً",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "رجوع")
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigateCall("call_$chatId", chatTitle, false) }) {
                        Icon(imageVector = Icons.Outlined.Call, contentDescription = "مكالمة صوتية", tint = SilaPrimary)
                    }
                    IconButton(onClick = { onNavigateCall("call_$chatId", chatTitle, true) }) {
                        Icon(imageVector = Icons.Outlined.Videocam, contentDescription = "مكالمة فيديو", tint = SilaPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Security Encryption Indicator Banner
            Surface(
                color = SilaPrimary.copy(alpha = 0.08f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = SilaPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "الرسائل والمكالمات مشفرة تماماً بين أطراف الاتصال 🔒",
                        fontSize = 11.sp,
                        color = SilaPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Chat Messages History List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(messages, key = { it.id }) { message ->
                    ChatMessageItem(
                        message = message,
                        onReactionSelect = { emoji ->
                            chatViewModel.addReaction(message.id, emoji)
                        }
                    )
                }
            }

            // Recording Voice Note Floating Bar
            VoiceNoteRecorderBar(
                isRecording = isRecordingVoice,
                onCancel = { isRecordingVoice = false },
                onSendRecordedVoice = { durationSec ->
                    chatViewModel.sendMessage(
                        chatId = chatId,
                        text = "رسالة صوتية",
                        type = MessageType.AUDIO_VOICE_NOTE,
                        durationSeconds = durationSec
                    )
                    isRecordingVoice = false
                }
            )

            // Input Bar
            if (!isRecordingVoice) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { showAttachmentSheet = true }) {
                            Icon(
                                imageVector = Icons.Default.AttachFile,
                                contentDescription = "إرفاق",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text("اكتب رسالتك...") },
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 44.dp, max = 110.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        if (inputText.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    val textToSend = inputText.trim()
                                    inputText = ""
                                    chatViewModel.sendMessage(
                                        chatId = chatId,
                                        text = textToSend,
                                        type = MessageType.TEXT
                                    )
                                },
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(SilaPrimary, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "إرسال",
                                    tint = Color.White
                                )
                            }
                        } else {
                            IconButton(
                                onClick = { isRecordingVoice = true },
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(SilaPrimary, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "تسجيل صوتي",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Attachment Options Sheet
    if (showAttachmentSheet) {
        ModalBottomSheet(onDismissRequest = { showAttachmentSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "مشاركة ملف أو محتوى 📎",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    AttachmentOptionItem("صورة", Icons.Default.Image, Color(0xFF10B981)) {
                        chatViewModel.sendMessage(
                            chatId = chatId,
                            text = "صورة مرفقة",
                            type = MessageType.IMAGE,
                            mediaUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=500"
                        )
                        showAttachmentSheet = false
                    }
                    AttachmentOptionItem("موقع", Icons.Default.LocationOn, Color(0xFFEF4444)) {
                        chatViewModel.sendMessage(
                            chatId = chatId,
                            text = "موقع جغرافي",
                            type = MessageType.LOCATION,
                            locationLat = 24.7136,
                            locationLng = 46.6753
                        )
                        showAttachmentSheet = false
                    }
                    AttachmentOptionItem("جهة اتصال", Icons.Default.AccountBox, Color(0xFF0284C7)) {
                        chatViewModel.sendMessage(
                            chatId = chatId,
                            text = "جهة اتصال",
                            type = MessageType.CONTACT,
                            contactName = "سارة أحمد",
                            contactPhone = "+966 50 888 7777"
                        )
                        showAttachmentSheet = false
                    }
                    AttachmentOptionItem("مستند", Icons.Default.InsertDriveFile, Color(0xFF7C3AED)) {
                        chatViewModel.sendMessage(
                            chatId = chatId,
                            text = "تقرير_المشروع.pdf",
                            type = MessageType.FILE
                        )
                        showAttachmentSheet = false
                    }
                }
            }
        }
    }
}

@Composable
fun AttachmentOptionItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bgColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(54.dp)
                .background(bgColor, CircleShape)
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = Color.White)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = title, fontSize = 12.sp)
    }
}
