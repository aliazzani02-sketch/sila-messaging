package com.example.domain.model

enum class MessageType {
    TEXT,
    IMAGE,
    VIDEO,
    AUDIO_VOICE_NOTE,
    FILE,
    LOCATION,
    CONTACT
}

enum class MessageStatus {
    SENDING,
    SENT,
    DELIVERED,
    READ
}

enum class CallStatus {
    INCOMING,
    OUTGOING,
    CONNECTED,
    ENDED,
    MISSED
}

data class User(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val email: String = "",
    val avatarUrl: String = "",
    val statusBio: String = "متصل عبر صلة ✨",
    val isOnline: Boolean = true,
    val lastSeen: Long = System.currentTimeMillis(),
    val isEmailLinked: Boolean = false
)

data class ChatMessage(
    val id: String,
    val chatId: String,
    val senderId: String,
    val senderName: String,
    val senderAvatar: String = "",
    val text: String,
    val messageType: MessageType = MessageType.TEXT,
    val mediaUrl: String = "",
    val durationSeconds: Int = 0,
    val locationLat: Double = 0.0,
    val locationLng: Double = 0.0,
    val contactName: String = "",
    val contactPhone: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isSentByMe: Boolean = false,
    val status: MessageStatus = MessageStatus.DELIVERED,
    val isEncrypted: Boolean = true,
    val reactionEmoji: String = ""
)

data class Chat(
    val id: String,
    val name: String,
    val avatarUrl: String = "",
    val isGroup: Boolean = false,
    val isCommunity: Boolean = false,
    val unreadCount: Int = 0,
    val lastMessageText: String = "",
    val lastMessageTime: Long = System.currentTimeMillis(),
    val isOnline: Boolean = false,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val isMuted: Boolean = false,
    val memberCount: Int = 2
)

data class Group(
    val id: String,
    val name: String,
    val description: String,
    val avatarUrl: String = "",
    val adminIds: List<String> = emptyList(),
    val memberIds: List<String> = emptyList(),
    val createdTime: Long = System.currentTimeMillis(),
    val pinnedNotice: String = ""
)

data class Community(
    val id: String,
    val name: String,
    val description: String,
    val avatarUrl: String = "",
    val channelNames: List<String> = listOf("الإعلانات العامة", "الدردشة العامة", "الفعاليات"),
    val memberCount: Int = 128,
    val isJoined: Boolean = false
)

data class CallLog(
    val id: String,
    val participantId: String,
    val participantName: String,
    val participantAvatar: String = "",
    val isVideoCall: Boolean = false,
    val callStatus: CallStatus = CallStatus.CONNECTED,
    val timestamp: Long = System.currentTimeMillis(),
    val durationSeconds: Int = 124
)

data class UserStatus(
    val id: String,
    val userId: String,
    val userName: String,
    val userAvatar: String = "",
    val mediaUrl: String = "",
    val caption: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isViewed: Boolean = false,
    val hasUnseenStory: Boolean = true
)

data class CountryCode(
    val name: String,
    val code: String,
    val flagEmoji: String
)
