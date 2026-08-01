package com.example.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SilaPrimary
import kotlinx.coroutines.delay

@Composable
fun VoiceNoteRecorderBar(
    isRecording: Boolean,
    onCancel: () -> Unit,
    onSendRecordedVoice: (durationSeconds: Int) -> Unit
) {
    var recordTimeSeconds by remember { mutableStateOf(0) }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            recordTimeSeconds = 0
            while (true) {
                delay(1000)
                recordTimeSeconds++
            }
        }
    }

    AnimatedVisibility(
        visible = isRecording,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = String.format("%02d:%02d", recordTimeSeconds / 60, recordTimeSeconds % 60),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "جاري التسجيل الصوتي...",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onCancel) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "إلغاء",
                        tint = Color.Red
                    )
                }
                IconButton(
                    onClick = { onSendRecordedVoice(recordTimeSeconds.coerceAtLeast(1)) },
                    modifier = Modifier.background(SilaPrimary, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "إرسال التسجيل",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
