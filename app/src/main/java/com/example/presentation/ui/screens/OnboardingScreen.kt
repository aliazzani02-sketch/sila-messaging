package com.example.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SilaPrimary
import com.example.ui.theme.SilaSecondary

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit
) {
    val pages = remember {
        listOf(
            OnboardingPage(
                title = "محادثات فورية وآمنة 💬",
                description = "تبادل الرسائل والصور والمستندات والتسجيلات الصوتية في بيئة عالية التشفير والاستقرار.",
                icon = Icons.Default.ChatBubble,
                color = SilaPrimary
            ),
            OnboardingPage(
                title = "مكالمات صوت وفيديو HD 📞",
                description = "تواصل صوتي ومرئي فائق الوضوح بتقنية WebRTC الحديثة مع التحكم الكامل بالميكروفون والكاميرا.",
                icon = Icons.Default.Call,
                color = SilaSecondary
            ),
            OnboardingPage(
                title = "مجموعات ومجتمعات 🌐",
                description = "أنشئ مجموعاتك وانضم إلى مجتمعات صلة لمتابعة القنوات والإعلانات وتفعيل النقاشات الجماهيرية.",
                icon = Icons.Default.Groups,
                color = Color(0xFF7C3AED)
            )
        )
    }

    var currentPage by remember { mutableStateOf(0) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar Skip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onFinishOnboarding) {
                    Text(
                        text = "تخطي",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp
                    )
                }
            }

            // Main Slide Content
            val page = pages[currentPage]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = page.color.copy(alpha = 0.15f),
                    modifier = Modifier.size(140.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = page.icon,
                            contentDescription = null,
                            tint = page.color,
                            modifier = Modifier.size(68.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = page.title,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = page.description,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }

            // Page Indicator Dots & Navigation Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    pages.indices.forEach { index ->
                        Box(
                            modifier = Modifier
                                .size(if (index == currentPage) 24.dp else 8.dp, 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index == currentPage) SilaPrimary else MaterialTheme.colorScheme.surfaceVariant
                                )
                        )
                    }
                }

                Button(
                    onClick = {
                        if (currentPage < pages.size - 1) {
                            currentPage++
                        } else {
                            onFinishOnboarding()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SilaPrimary)
                ) {
                    Text(
                        text = if (currentPage == pages.size - 1) "بدء استخدام صلة" else "التالي",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
