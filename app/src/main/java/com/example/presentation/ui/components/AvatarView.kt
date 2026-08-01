package com.example.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.SilaOnlineGreen
import com.example.ui.theme.SilaPrimary

@Composable
fun AvatarView(
    name: String,
    avatarUrl: String = "",
    size: Dp = 52.dp,
    isOnline: Boolean = false,
    showStatusDot: Boolean = true
) {
    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.BottomEnd
    ) {
        if (avatarUrl.isNotBlank()) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .border(1.5.dp, SilaPrimary.copy(alpha = 0.3f), CircleShape)
            )
        } else {
            // Generative Initial Avatar
            val initial = name.trim().take(1).uppercase()
            Surface(
                shape = CircleShape,
                color = SilaPrimary.copy(alpha = 0.85f),
                modifier = Modifier.size(size)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (initial.isNotBlank()) {
                        Text(
                            text = initial,
                            color = Color.White,
                            fontSize = (size.value * 0.42f).sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(size * 0.5f)
                        )
                    }
                }
            }
        }

        if (showStatusDot && isOnline) {
            Box(
                modifier = Modifier
                    .size(size * 0.28f)
                    .clip(CircleShape)
                    .background(SilaOnlineGreen)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
            )
        }
    }
}
