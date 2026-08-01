package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val chatId: String,
    val senderId: String,
    val senderName: String,
    val senderAvatar: String,
    val text: String,
    val messageType: String,
    val mediaUrl: String,
    val durationSeconds: Int,
    val locationLat: Double,
    val locationLng: Double,
    val contactName: String,
    val contactPhone: String,
    val timestamp: Long,
    val isSentByMe: Boolean,
    val status: String,
    val isEncrypted: Boolean,
    val reactionEmoji: String
)

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val id: String,
    val name: String,
    val avatarUrl: String,
    val isGroup: Boolean,
    val isCommunity: Boolean,
    val unreadCount: Int,
    val lastMessageText: String,
    val lastMessageTime: Long,
    val isOnline: Boolean,
    val isPinned: Boolean,
    val isArchived: Boolean,
    val isMuted: Boolean,
    val memberCount: Int
)

@Entity(tableName = "call_logs")
data class CallEntity(
    @PrimaryKey val id: String,
    val participantId: String,
    val participantName: String,
    val participantAvatar: String,
    val isVideoCall: Boolean,
    val callStatus: String,
    val timestamp: Long,
    val durationSeconds: Int
)

@Entity(tableName = "statuses")
data class StatusEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val userName: String,
    val userAvatar: String,
    val mediaUrl: String,
    val caption: String,
    val timestamp: Long,
    val isViewed: Boolean
)
