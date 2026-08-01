package com.example.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.CallLog
import com.example.domain.model.Chat
import com.example.domain.model.UserStatus
import com.example.presentation.ui.components.AvatarView
import com.example.presentation.ui.components.SilaBottomBar
import com.example.presentation.ui.components.SilaTab
import com.example.presentation.ui.components.SilaTopAppBar
import com.example.presentation.viewmodel.CallsViewModel
import com.example.presentation.viewmodel.ChatViewModel
import com.example.presentation.viewmodel.SilaViewModel
import com.example.presentation.viewmodel.StatusViewModel
import com.example.ui.theme.SilaOnlineGreen
import com.example.ui.theme.SilaPrimary
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    silaViewModel: SilaViewModel,
    chatViewModel: ChatViewModel,
    callsViewModel: CallsViewModel,
    statusViewModel: StatusViewModel,
    onNavigateChatDetail: (chatId: String) -> Unit,
    onNavigateCallScreen: (callId: String, name: String, isVideo: Boolean) -> Unit,
    onNavigateStatusViewer: (statusId: String) -> Unit,
    onNavigateCreateGroup: () -> Unit,
    onNavigateSearch: () -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigateContacts: () -> Unit
) {
    val isDarkMode by silaViewModel.isDarkMode.collectAsState()
    val chats by chatViewModel.filteredChats.collectAsState()
    val allChats by chatViewModel.chats.collectAsState()
    val calls by callsViewModel.callLogs.collectAsState()
    val statuses by statusViewModel.statuses.collectAsState()
    val selectedFilterTab by chatViewModel.selectedTab.collectAsState()

    var activeBottomTab by remember { mutableStateOf(SilaTab.CHATS) }

    val totalUnread = remember(allChats) {
        allChats.sumOf { it.unreadCount }
    }

    Scaffold(
        topBar = {
            SilaTopAppBar(
                title = when (activeBottomTab) {
                    SilaTab.CHATS -> "صلة"
                    SilaTab.GROUPS -> "المجموعات"
                    SilaTab.COMMUNITIES -> "المجتمعات"
                    SilaTab.CALLS -> "المكالمات"
                    SilaTab.STATUS -> "الحالات"
                },
                isDarkMode = isDarkMode,
                onToggleDarkMode = { silaViewModel.toggleDarkMode(!isDarkMode) },
                onSearchClick = onNavigateSearch,
                onSettingsClick = onNavigateSettings,
                onNewGroupClick = onNavigateCreateGroup
            )
        },
        bottomBar = {
            SilaBottomBar(
                selectedTab = activeBottomTab,
                onTabSelected = { activeBottomTab = it },
                unreadChatsCount = totalUnread
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    when (activeBottomTab) {
                        SilaTab.CHATS -> onNavigateContacts()
                        SilaTab.GROUPS -> onNavigateCreateGroup()
                        SilaTab.CALLS -> onNavigateContacts()
                        SilaTab.STATUS -> statusViewModel.postStatus("يوم رائع جداً متصل عبر تطبيق صلة ✨")
                        else -> onNavigateContacts()
                    }
                },
                containerColor = SilaPrimary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = when (activeBottomTab) {
                        SilaTab.CHATS -> Icons.Default.Chat
                        SilaTab.GROUPS -> Icons.Default.GroupAdd
                        SilaTab.CALLS -> Icons.Default.Call
                        SilaTab.STATUS -> Icons.Default.AddAPhoto
                        else -> Icons.Default.Add
                    },
                    contentDescription = "إضافة"
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeBottomTab) {
                SilaTab.CHATS -> ChatsTabContent(
                    chats = chats,
                    selectedFilterTab = selectedFilterTab,
                    onSelectFilterTab = { chatViewModel.selectTab(it) },
                    onChatClick = { chat ->
                        chatViewModel.markAsRead(chat.id)
                        onNavigateChatDetail(chat.id)
                    }
                )

                SilaTab.GROUPS -> GroupsTabContent(
                    groups = allChats.filter { it.isGroup },
                    onChatClick = { onNavigateChatDetail(it.id) },
                    onCreateGroupClick = onNavigateCreateGroup
                )

                SilaTab.COMMUNITIES -> CommunitiesTabContent(
                    communities = allChats.filter { it.isCommunity },
                    onChatClick = { onNavigateChatDetail(it.id) }
                )

                SilaTab.CALLS -> CallsTabContent(
                    calls = calls,
                    onCallClick = { call ->
                        onNavigateCallScreen(call.id, call.participantName, call.isVideoCall)
                    }
                )

                SilaTab.STATUS -> StatusTabContent(
                    statuses = statuses,
                    onStatusClick = { onNavigateStatusViewer(it.id) },
                    onPostStatusClick = { statusViewModel.postStatus("حالة جديدة عبر صلة ✨") }
                )
            }
        }
    }
}

@Composable
fun ChatsTabContent(
    chats: List<Chat>,
    selectedFilterTab: Int,
    onSelectFilterTab: (Int) -> Unit,
    onChatClick: (Chat) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Filter Chips Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filterOptions = listOf("الكل", "غير مقروءة", "المجموعات", "المجتمعات")
            filterOptions.forEachIndexed { index, title ->
                val isSelected = index == selectedFilterTab
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectFilterTab(index) },
                    label = {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SilaPrimary,
                        selectedLabelColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        if (chats.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "لا توجد محادثات في هذا القسم",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(chats) { chat ->
                    ChatItemRow(chat = chat, onClick = { onChatClick(chat) })
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.padding(start = 76.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatItemRow(
    chat: Chat,
    onClick: () -> Unit
) {
    val formattedTime = remember(chat.lastMessageTime) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(chat.lastMessageTime))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarView(
            name = chat.name,
            avatarUrl = chat.avatarUrl,
            size = 52.dp,
            isOnline = chat.isOnline,
            showStatusDot = !chat.isGroup && !chat.isCommunity
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (chat.isPinned) {
                        Icon(
                            imageVector = Icons.Default.PushPin,
                            contentDescription = "مثبت",
                            tint = SilaPrimary,
                            modifier = Modifier.size(14.dp).padding(end = 4.dp)
                        )
                    }
                    Text(
                        text = chat.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = formattedTime,
                    fontSize = 12.sp,
                    color = if (chat.unreadCount > 0) SilaPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.lastMessageText,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (chat.unreadCount > 0) {
                    Surface(
                        shape = CircleShape,
                        color = SilaPrimary,
                        modifier = Modifier.padding(start = 6.dp)
                    ) {
                        Text(
                            text = "${chat.unreadCount}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GroupsTabContent(
    groups: List<Chat>,
    onChatClick: (Chat) -> Unit,
    onCreateGroupClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            onClick = onCreateGroupClick,
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SilaPrimary.copy(alpha = 0.1f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.GroupAdd,
                    contentDescription = null,
                    tint = SilaPrimary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "إنشاء مجموعة جديدة",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "تواصل مع عدة أصدقاء أو زملاء في مكان واحد",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(groups) { group ->
                ChatItemRow(chat = group, onClick = { onChatClick(group) })
            }
        }
    }
}

@Composable
fun CommunitiesTabContent(
    communities: List<Chat>,
    onChatClick: (Chat) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 8.dp)) {
        items(communities) { comm ->
            Card(
                onClick = { onChatClick(comm) },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AvatarView(
                        name = comm.name,
                        avatarUrl = comm.avatarUrl,
                        size = 54.dp
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = comm.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "تضم ${comm.memberCount} عضواً • 3 قنوات تواصل",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = comm.lastMessageText,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CallsTabContent(
    calls: List<CallLog>,
    onCallClick: (CallLog) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 8.dp)) {
        items(calls) { call ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCallClick(call) }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AvatarView(
                        name = call.participantName,
                        avatarUrl = call.participantAvatar,
                        size = 48.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = call.participantName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (call.isVideoCall) Icons.Default.Videocam else Icons.Default.Call,
                                contentDescription = null,
                                tint = SilaPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (call.isVideoCall) "مكالمة فيديو" else "مكالمة صوتية",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                IconButton(onClick = { onCallClick(call) }) {
                    Icon(
                        imageVector = if (call.isVideoCall) Icons.Default.Videocam else Icons.Default.Call,
                        contentDescription = "اتصال",
                        tint = SilaPrimary
                    )
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        }
    }
}

@Composable
fun StatusTabContent(
    statuses: List<UserStatus>,
    onStatusClick: (UserStatus) -> Unit,
    onPostStatusClick: () -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            // My Status Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPostStatusClick() }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    AvatarView(name = "أنت", size = 56.dp)
                    Surface(
                        shape = CircleShape,
                        color = SilaPrimary,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "إضافة حالة",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "حالتك الشخصية",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "انقر لإضافة التحديثات اليومية",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "التحديثات الأخيرة للأصدقاء",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(statuses) { status ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStatusClick(status) }
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AvatarView(
                    name = status.userName,
                    avatarUrl = status.userAvatar,
                    size = 54.dp
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = status.userName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = status.caption.ifBlank { "تحديث حالة جديد" },
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        }
    }
}
