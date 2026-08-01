package com.example.presentation.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SilaPrimary

enum class SilaTab(val title: String, val activeIcon: ImageVector, val inactiveIcon: ImageVector) {
    CHATS("محادثات", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline),
    GROUPS("مجموعات", Icons.Filled.Groups, Icons.Outlined.Groups),
    COMMUNITIES("مجتمعات", Icons.Filled.Public, Icons.Outlined.Public),
    CALLS("مكالمات", Icons.Filled.Call, Icons.Outlined.Call),
    STATUS("حالات", Icons.Filled.MotionPhotosOn, Icons.Outlined.MotionPhotosAuto)
}

@Composable
fun SilaBottomBar(
    selectedTab: SilaTab,
    onTabSelected: (SilaTab) -> Unit,
    unreadChatsCount: Int = 0
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        SilaTab.values().forEach { tab ->
            val isSelected = tab == selectedTab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (tab == SilaTab.CHATS && unreadChatsCount > 0) {
                                Badge(
                                    containerColor = SilaPrimary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) {
                                    Text(text = "$unreadChatsCount")
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isSelected) tab.activeIcon else tab.inactiveIcon,
                            contentDescription = tab.title
                        )
                    }
                },
                label = {
                    Text(
                        text = tab.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SilaPrimary,
                    selectedTextColor = SilaPrimary,
                    indicatorColor = SilaPrimary.copy(alpha = 0.15f)
                )
            )
        }
    }
}
