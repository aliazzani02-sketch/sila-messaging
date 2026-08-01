package com.example.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
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
import com.example.presentation.ui.components.AvatarView
import com.example.presentation.viewmodel.StatusViewModel
import com.example.ui.theme.SilaPrimary
import kotlinx.coroutines.delay

@Composable
fun StatusViewerScreen(
    statusId: String,
    statusViewModel: StatusViewModel,
    onNavigateBack: () -> Unit
) {
    val statuses by statusViewModel.statuses.collectAsState()
    val status = remember(statuses, statusId) {
        statuses.find { it.id == statusId } ?: statuses.firstOrNull()
    }

    var progress by remember { mutableFloatStateOf(0f) }
    var replyText by remember { mutableStateOf("") }

    LaunchedEffect(statusId) {
        statusViewModel.markViewed(statusId)
        progress = 0f
        while (progress < 1.0f) {
            delay(50)
            progress += 0.01f
        }
        onNavigateBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Media background image or text card
        if (status?.mediaUrl?.isNotBlank() == true) {
            AsyncImage(
                model = status.mediaUrl,
                contentDescription = status.caption,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SilaPrimary.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = status?.caption ?: "حالة صلة جديدة",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(32.dp)
                )
            }
        }

        // Top Progress Line & User Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp)
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = SilaPrimary,
                trackColor = Color.White.copy(alpha = 0.4f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AvatarView(
                        name = status?.userName ?: "مستخدم صلة",
                        avatarUrl = status?.userAvatar ?: "",
                        size = 40.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = status?.userName ?: "مستخدم صلة",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "منذ ساعة واحدة",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "إغلاق",
                        tint = Color.White
                    )
                }
            }
        }

        // Caption overlay if image present
        if (status?.mediaUrl?.isNotBlank() == true && status.caption.isNotBlank()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 90.dp, start = 16.dp, end = 16.dp)
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = status.caption,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }

        // Reply Bar
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = replyText,
                onValueChange = { replyText = it },
                placeholder = { Text("الرد على الحالة...", color = Color.White.copy(alpha = 0.7f)) },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(max = 50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    replyText = ""
                    onNavigateBack()
                },
                modifier = Modifier.background(SilaPrimary, RoundedCornerShape(24.dp))
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "إرسال", tint = Color.White)
            }
        }
    }
}
