package com.example.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.model.ChatMessage
import com.example.domain.model.MessageStatus
import com.example.domain.model.MessageType
import com.example.ui.theme.SilaPrimary
import com.example.ui.theme.SilaSentBubbleDark
import com.example.ui.theme.SilaSentBubbleLight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    onReactionSelect: (String) -> Unit = {}
) {
    var isPlayingVoice by remember { mutableStateOf(false) }
    var showReactionPicker by remember { mutableStateOf(false) }

    val formattedTime = remember(message.timestamp) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp))
    }

    val bubbleShape = if (message.isSentByMe) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp)
    } else {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp)
    }

    val bubbleBg = if (message.isSentByMe) {
        if (MaterialTheme.colorScheme.surface == Color(0xFF1E293B) || MaterialTheme.colorScheme.background == Color(0xFF0F172A)) {
            SilaSentBubbleDark
        } else {
            SilaSentBubbleLight
        }
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = if (message.isSentByMe) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 12.dp),
        horizontalAlignment = if (message.isSentByMe) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = bubbleShape,
            color = bubbleBg,
            shadowElevation = 1.dp,
            modifier = Modifier
                .widthIn(max = 290.dp)
                .clickable { showReactionPicker = !showReactionPicker }
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                if (!message.isSentByMe && message.senderName.isNotBlank()) {
                    Text(
                        text = message.senderName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SilaPrimary,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                when (message.messageType) {
                    MessageType.TEXT -> {
                        Text(
                            text = message.text,
                            fontSize = 15.sp,
                            color = textColor,
                            lineHeight = 20.sp
                        )
                    }

                    MessageType.AUDIO_VOICE_NOTE -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = { isPlayingVoice = !isPlayingVoice },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(SilaPrimary)
                            ) {
                                Icon(
                                    imageVector = if (isPlayingVoice) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "تشغيل الصوت",
                                    tint = Color.White
                                )
                            }
                            Column {
                                Text(
                                    text = if (isPlayingVoice) "جاري التشغيل..." else "رسالة صوتية",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                                LinearProgressIndicator(
                                    progress = { if (isPlayingVoice) 0.6f else 0.0f },
                                    modifier = Modifier
                                        .width(130.dp)
                                        .height(4.dp)
                                        .padding(top = 4.dp),
                                    color = SilaPrimary,
                                )
                            }
                            Text(
                                text = "${message.durationSeconds}ث",
                                fontSize = 11.sp,
                                color = textColor.copy(alpha = 0.7f)
                            )
                        }
                    }

                    MessageType.IMAGE -> {
                        Column {
                            if (message.mediaUrl.isNotBlank()) {
                                AsyncImage(
                                    model = message.mediaUrl,
                                    contentDescription = "صورة مرفقة",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            }
                            if (message.text.isNotBlank()) {
                                Text(
                                    text = message.text,
                                    fontSize = 14.sp,
                                    color = textColor,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }

                    MessageType.LOCATION -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(28.dp)
                            )
                            Column {
                                Text(
                                    text = "موقع جغرافي مباشر 📍",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                                Text(
                                    text = "إحداثيات: 24.7136° N, 46.6753° E",
                                    fontSize = 11.sp,
                                    color = textColor.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    MessageType.CONTACT -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBox,
                                contentDescription = null,
                                tint = SilaPrimary,
                                modifier = Modifier.size(30.dp)
                            )
                            Column {
                                Text(
                                    text = message.contactName.ifBlank { "جهة اتصال" },
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                                Text(
                                    text = message.contactPhone.ifBlank { "+966 55 987 6543" },
                                    fontSize = 11.sp,
                                    color = textColor.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    else -> {
                        Text(
                            text = message.text,
                            fontSize = 14.sp,
                            color = textColor
                        )
                    }
                }

                // Footer Info (Time, Encryption Lock, Read Status)
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (message.isEncrypted) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "مشفر",
                            tint = textColor.copy(alpha = 0.5f),
                            modifier = Modifier.size(11.dp)
                        )
                    }
                    Text(
                        text = formattedTime,
                        fontSize = 10.sp,
                        color = textColor.copy(alpha = 0.7f)
                    )
                    if (message.isSentByMe) {
                        val (icon, color) = when (message.status) {
                            MessageStatus.SENDING -> Icons.Default.AccessTime to Color.Gray
                            MessageStatus.SENT -> Icons.Default.Check to Color.Gray
                            MessageStatus.DELIVERED -> Icons.Default.DoneAll to Color.Gray
                            MessageStatus.READ -> Icons.Default.DoneAll to SilaPrimary
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // Reaction Emoji Badge if present
        if (message.reactionEmoji.isNotBlank()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .padding(top = 2.dp, start = 8.dp)
            ) {
                Text(
                    text = message.reactionEmoji,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        // Quick Emoji Reaction Bar (toggled on click)
        if (showReactionPicker) {
            Row(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, SilaPrimary.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("❤️", "👍", "😂", "😮", "🙏", "🔥").forEach { emoji ->
                    Text(
                        text = emoji,
                        fontSize = 18.sp,
                        modifier = Modifier.clickable {
                            onReactionSelect(emoji)
                            showReactionPicker = false
                        }
                    )
                }
            }
        }
    }
}
