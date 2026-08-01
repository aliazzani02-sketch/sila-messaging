package com.example.presentation.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.ui.components.AvatarView
import com.example.ui.theme.SilaDarkBackground
import com.example.ui.theme.SilaPrimary
import kotlinx.coroutines.delay

@Composable
fun CallScreen(
    callId: String,
    participantName: String,
    isVideoCall: Boolean,
    onEndCall: () -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(true) }
    var isCameraOff by remember { mutableStateOf(!isVideoCall) }
    var durationSeconds by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            durationSeconds++
        }
    }

    val formattedDuration = remember(durationSeconds) {
        String.format("%02d:%02d", durationSeconds / 60, durationSeconds % 60)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E1B4B),
                        Color(0xFF090D16)
                    )
                )
            )
    ) {
        // Main Video / Avatar Background Surface
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AvatarView(
                name = participantName,
                size = 130.dp,
                isOnline = true,
                showStatusDot = false
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = participantName,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = CircleShape,
                color = SilaPrimary.copy(alpha = 0.2f),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "جاري الاتصال آمن بـ صلة • $formattedDuration",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }

        // Bottom Controls Bar
        Surface(
            color = Color.Black.copy(alpha = 0.4f),
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mic Mute Button
                IconButton(
                    onClick = { isMuted = !isMuted },
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(if (isMuted) Color.White else Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "كتم الميكروفون",
                        tint = if (isMuted) Color.Black else Color.White
                    )
                }

                // Camera Toggle Button
                IconButton(
                    onClick = { isCameraOff = !isCameraOff },
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(if (isCameraOff) Color.White.copy(alpha = 0.2f) else Color.White)
                ) {
                    Icon(
                        imageVector = if (isCameraOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                        contentDescription = "الكاميرا",
                        tint = if (isCameraOff) Color.White else Color.Black
                    )
                }

                // Speaker Button
                IconButton(
                    onClick = { isSpeakerOn = !isSpeakerOn },
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(if (isSpeakerOn) SilaPrimary else Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = if (isSpeakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "المكبر",
                        tint = Color.White
                    )
                }

                // End Call Red Button
                IconButton(
                    onClick = onEndCall,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444))
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "إنهاء المكالمة",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
