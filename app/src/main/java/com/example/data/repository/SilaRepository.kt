package com.example.data.repository

import com.example.data.local.*
import com.example.domain.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class SilaRepository(
    private val appDatabase: AppDatabase
) {
    private val messageDao = appDatabase.messageDao()
    private val chatDao = appDatabase.chatDao()
    private val callDao = appDatabase.callDao()
    private val statusDao = appDatabase.statusDao()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfEmpty()
        }
    }

    val chats: Flow<List<Chat>> = chatDao.getAllChats().map { entities ->
        entities.map { it.toDomain() }
    }

    val calls: Flow<List<CallLog>> = callDao.getAllCalls().map { entities ->
        entities.map { it.toDomain() }
    }

    val statuses: Flow<List<UserStatus>> = statusDao.getAllStatuses().map { entities ->
        entities.map { it.toDomain() }
    }

    fun getMessages(chatId: String): Flow<List<ChatMessage>> =
        messageDao.getMessagesForChat(chatId).map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun sendMessage(
        chatId: String,
        text: String,
        type: MessageType = MessageType.TEXT,
        mediaUrl: String = "",
        durationSeconds: Int = 0,
        locationLat: Double = 0.0,
        locationLng: Double = 0.0,
        contactName: String = "",
        contactPhone: String = ""
    ) {
        val messageId = UUID.randomUUID().toString()
        val timestamp = System.currentTimeMillis()
        val messageEntity = ChatMessageEntity(
            id = messageId,
            chatId = chatId,
            senderId = "me",
            senderName = "أنت",
            senderAvatar = "",
            text = text,
            messageType = type.name,
            mediaUrl = mediaUrl,
            durationSeconds = durationSeconds,
            locationLat = locationLat,
            locationLng = locationLng,
            contactName = contactName,
            contactPhone = contactPhone,
            timestamp = timestamp,
            isSentByMe = true,
            status = MessageStatus.SENT.name,
            isEncrypted = true,
            reactionEmoji = ""
        )
        messageDao.insertMessage(messageEntity)

        val lastTextSummary = when (type) {
            MessageType.TEXT -> text
            MessageType.IMAGE -> "📷 صورة"
            MessageType.VIDEO -> "🎥 فيديو"
            MessageType.AUDIO_VOICE_NOTE -> "🎤 رسالة صوتية ($durationSeconds ث)"
            MessageType.FILE -> "📄 مستند"
            MessageType.LOCATION -> "📍 موقع جغرافي"
            MessageType.CONTACT -> "👤 جهة اتصال ($contactName)"
        }

        chatDao.updateLastMessage(chatId, lastTextSummary, timestamp)

        // Simulated reply bot after 1.5s for dynamic interactive feel!
        CoroutineScope(Dispatchers.IO).launch {
            kotlinx.coroutines.delay(1500)
            val replyId = UUID.randomUUID().toString()
            val replyText = generateBotReply(text, type)
            val replyTime = System.currentTimeMillis()
            val replyEntity = ChatMessageEntity(
                id = replyId,
                chatId = chatId,
                senderId = "contact_$chatId",
                senderName = "صديق صلة",
                senderAvatar = "",
                text = replyText,
                messageType = MessageType.TEXT.name,
                mediaUrl = "",
                durationSeconds = 0,
                locationLat = 0.0,
                locationLng = 0.0,
                contactName = "",
                contactPhone = "",
                timestamp = replyTime,
                isSentByMe = false,
                status = MessageStatus.READ.name,
                isEncrypted = true,
                reactionEmoji = ""
            )
            messageDao.insertMessage(replyEntity)
            chatDao.updateLastMessage(chatId, replyText, replyTime)
        }
    }

    suspend fun updateReaction(messageId: String, emoji: String) {
        messageDao.updateReaction(messageId, emoji)
    }

    suspend fun clearUnread(chatId: String) {
        chatDao.clearUnreadCount(chatId)
    }

    suspend fun createNewGroup(name: String, description: String, selectedContactNames: List<String>) {
        val groupId = "group_${UUID.randomUUID()}"
        val chatEntity = ChatEntity(
            id = groupId,
            name = name,
            avatarUrl = "",
            isGroup = true,
            isCommunity = false,
            unreadCount = 0,
            lastMessageText = "تم إنشاء المجموعة بواسطة أنت",
            lastMessageTime = System.currentTimeMillis(),
            isOnline = true,
            isPinned = false,
            isArchived = false,
            isMuted = false,
            memberCount = selectedContactNames.size + 1
        )
        chatDao.insertChat(chatEntity)

        val welcomeMessage = ChatMessageEntity(
            id = UUID.randomUUID().toString(),
            chatId = groupId,
            senderId = "system",
            senderName = "النظام",
            senderAvatar = "",
            text = "مرحباً بكم في مجموعة $name! $description",
            messageType = MessageType.TEXT.name,
            mediaUrl = "",
            durationSeconds = 0,
            locationLat = 0.0,
            locationLng = 0.0,
            contactName = "",
            contactPhone = "",
            timestamp = System.currentTimeMillis(),
            isSentByMe = false,
            status = MessageStatus.READ.name,
            isEncrypted = true,
            reactionEmoji = ""
        )
        messageDao.insertMessage(welcomeMessage)
    }

    suspend fun postStatus(caption: String, mediaUrl: String = "") {
        val statusId = UUID.randomUUID().toString()
        val statusEntity = StatusEntity(
            id = statusId,
            userId = "me",
            userName = "حالتك الشخصية",
            userAvatar = "",
            mediaUrl = mediaUrl,
            caption = caption,
            timestamp = System.currentTimeMillis(),
            isViewed = false
        )
        statusDao.insertStatus(statusEntity)
    }

    suspend fun markStatusViewed(statusId: String) {
        statusDao.markAsViewed(statusId)
    }

    suspend fun addCallRecord(participantName: String, isVideo: Boolean) {
        val callEntity = CallEntity(
            id = UUID.randomUUID().toString(),
            participantId = "user_call",
            participantName = participantName,
            participantAvatar = "",
            isVideoCall = isVideo,
            callStatus = CallStatus.OUTGOING.name,
            timestamp = System.currentTimeMillis(),
            durationSeconds = 45
        )
        callDao.insertCall(callEntity)
    }

    private fun generateBotReply(userText: String, type: MessageType): String {
        return when (type) {
            MessageType.AUDIO_VOICE_NOTE -> "أهلاً بك! سمعت رسالتك الصوتية الجميلة. شكراً لسلامك عبر صلة! 🎧"
            MessageType.IMAGE -> "صورة رائعة جداً! تم استلام المرفق بنجاح وتشفيره بأمان. 📸"
            MessageType.LOCATION -> "تم استلام الموقع الجغرافي. سأكون هناك في الموعد المظبوط! 📍"
            MessageType.CONTACT -> "شكراً لمشاركة جهة الاتصال! سأتواصل معهم قريباً. 👤"
            MessageType.FILE -> "تم تنزيل الملف وحفظه بنجاح في التطبيق. 📄"
            else -> {
                if (userText.contains("مرحبا") || userText.contains("السلام")) {
                    "وعليكم السلام ورحمة الله وبركاته! أهلاً بك في صلة ✨ كيف يمكنني مساعدتك اليوم؟"
                } else if (userText.contains("كيف") || userText.contains("أخبارك")) {
                    "الحمد لله بخير ونعمة! أتمنى لك يوماً رائعاً ومتصلاً دائماً 🌟"
                } else {
                    "وصلت رسالتك: '$userText' — تم إرسالها وتشفيرها بطرفي الاتصال عبر شبكة صلة 🔐"
                }
            }
        }
    }

    private suspend fun seedInitialDataIfEmpty() {
        val existingChats = chatDao.getChatById("chat_1")
        if (existingChats != null) return // Already seeded

        val now = System.currentTimeMillis()
        val minute = 60 * 1000L
        val hour = 60 * minute

        // Seed Chats
        val initialChats = listOf(
            ChatEntity(
                id = "chat_1",
                name = "أحمد المحمدي",
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200",
                isGroup = false,
                isCommunity = false,
                unreadCount = 2,
                lastMessageText = "السلام عليكم، هل سنلتقي اليوم في المؤتمر؟",
                lastMessageTime = now - 5 * minute,
                isOnline = true,
                isPinned = true,
                isArchived = false,
                isMuted = false,
                memberCount = 2
            ),
            ChatEntity(
                id = "chat_2",
                name = "فريق تطوير صلة 🚀",
                avatarUrl = "https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=200",
                isGroup = true,
                isCommunity = false,
                unreadCount = 5,
                lastMessageText = "سارة: تم تحديث واجهات التشفير والتصميم الفاخر بنجاح!",
                lastMessageTime = now - 22 * minute,
                isOnline = true,
                isPinned = true,
                isArchived = false,
                isMuted = false,
                memberCount = 14
            ),
            ChatEntity(
                id = "chat_3",
                name = "مجتمع تقنية المعلومات 🌐",
                avatarUrl = "https://images.unsplash.com/photo-1519389950473-47ba0277781c?w=200",
                isGroup = false,
                isCommunity = true,
                unreadCount = 0,
                lastMessageText = "إعلان: بدء التسجيل في ملتقى الذكاء الاصطناعي الأسبوع القادم",
                lastMessageTime = now - 2 * hour,
                isOnline = false,
                isPinned = false,
                isArchived = false,
                isMuted = true,
                memberCount = 340
            ),
            ChatEntity(
                id = "chat_4",
                name = "نورة خالد",
                avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200",
                isGroup = false,
                isCommunity = false,
                unreadCount = 0,
                lastMessageText = "شكراً جزيلاً على المساعدة الطيب 🌟",
                lastMessageTime = now - 5 * hour,
                isOnline = false,
                isPinned = false,
                isArchived = false,
                isMuted = false,
                memberCount = 2
            ),
            ChatEntity(
                id = "chat_5",
                name = "عائلة السعادة 💖",
                avatarUrl = "https://images.unsplash.com/photo-1511895426328-dc8714191300?w=200",
                isGroup = true,
                isCommunity = false,
                unreadCount = 0,
                lastMessageText = "الوالد: جمعة مباركة على الجميع",
                lastMessageTime = now - 24 * hour,
                isOnline = true,
                isPinned = false,
                isArchived = false,
                isMuted = false,
                memberCount = 8
            )
        )
        chatDao.insertChats(initialChats)

        // Seed Chat Messages for chat_1
        val messagesChat1 = listOf(
            ChatMessageEntity(
                id = "msg_1_1",
                chatId = "chat_1",
                senderId = "contact_chat_1",
                senderName = "أحمد المحمدي",
                senderAvatar = "",
                text = "مرحباً! أهلاً بك في تطبيق صلة الجديد 🌟",
                messageType = MessageType.TEXT.name,
                mediaUrl = "",
                durationSeconds = 0,
                locationLat = 0.0,
                locationLng = 0.0,
                contactName = "",
                contactPhone = "",
                timestamp = now - 30 * minute,
                isSentByMe = false,
                status = MessageStatus.READ.name,
                isEncrypted = true,
                reactionEmoji = "❤️"
            ),
            ChatMessageEntity(
                id = "msg_1_2",
                chatId = "chat_1",
                senderId = "me",
                senderName = "أنت",
                senderAvatar = "",
                text = "أهلاً أحمد! التصميم والأداء ممتاز جداً ومحمي بالتشفير 🔒",
                messageType = MessageType.TEXT.name,
                mediaUrl = "",
                durationSeconds = 0,
                locationLat = 0.0,
                locationLng = 0.0,
                contactName = "",
                contactPhone = "",
                timestamp = now - 25 * minute,
                isSentByMe = true,
                status = MessageStatus.READ.name,
                isEncrypted = true,
                reactionEmoji = "👍"
            ),
            ChatMessageEntity(
                id = "msg_1_3",
                chatId = "chat_1",
                senderId = "contact_chat_1",
                senderName = "أحمد المحمدي",
                senderAvatar = "",
                text = "استمع إلى الملاحظة الصوتية الخاصة بالاجتماع:",
                messageType = MessageType.AUDIO_VOICE_NOTE.name,
                mediaUrl = "voice_sample.mp3",
                durationSeconds = 14,
                locationLat = 0.0,
                locationLng = 0.0,
                contactName = "",
                contactPhone = "",
                timestamp = now - 15 * minute,
                isSentByMe = false,
                status = MessageStatus.READ.name,
                isEncrypted = true,
                reactionEmoji = ""
            ),
            ChatMessageEntity(
                id = "msg_1_4",
                chatId = "chat_1",
                senderId = "contact_chat_1",
                senderName = "أحمد المحمدي",
                senderAvatar = "",
                text = "السلام عليكم، هل سنلتقي اليوم في المؤتمر؟",
                messageType = MessageType.TEXT.name,
                mediaUrl = "",
                durationSeconds = 0,
                locationLat = 0.0,
                locationLng = 0.0,
                contactName = "",
                contactPhone = "",
                timestamp = now - 5 * minute,
                isSentByMe = false,
                status = MessageStatus.DELIVERED.name,
                isEncrypted = true,
                reactionEmoji = ""
            )
        )
        messageDao.insertMessages(messagesChat1)

        // Seed Calls
        val initialCalls = listOf(
            CallEntity(
                id = "call_1",
                participantId = "user_1",
                participantName = "أحمد المحمدي",
                participantAvatar = "",
                isVideoCall = true,
                callStatus = CallStatus.CONNECTED.name,
                timestamp = now - 2 * hour,
                durationSeconds = 245
            ),
            CallEntity(
                id = "call_2",
                participantId = "user_4",
                participantName = "نورة خالد",
                participantAvatar = "",
                isVideoCall = false,
                callStatus = CallStatus.MISSED.name,
                timestamp = now - 18 * hour,
                durationSeconds = 0
            ),
            CallEntity(
                id = "call_3",
                participantId = "user_1",
                participantName = "أحمد المحمدي",
                participantAvatar = "",
                isVideoCall = false,
                callStatus = CallStatus.OUTGOING.name,
                timestamp = now - 2 * 24 * hour,
                durationSeconds = 120
            )
        )
        initialCalls.forEach { callDao.insertCall(it) }

        // Seed Statuses
        val initialStatuses = listOf(
            StatusEntity(
                id = "status_1",
                userId = "user_1",
                userName = "أحمد المحمدي",
                userAvatar = "",
                mediaUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=500",
                caption = "يوم مشرق على شاطئ البحر 🌊☀️",
                timestamp = now - 1 * hour,
                isViewed = false
            ),
            StatusEntity(
                id = "status_2",
                userId = "user_4",
                userName = "نورة خالد",
                userAvatar = "",
                mediaUrl = "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=500",
                caption = "قهوة الصباح وقراءة المجلد الجديد ☕️📚",
                timestamp = now - 3 * hour,
                isViewed = true
            )
        )
        initialStatuses.forEach { statusDao.insertStatus(it) }
    }

    // Mapping Extension Functions
    private fun ChatMessageEntity.toDomain() = ChatMessage(
        id = id,
        chatId = chatId,
        senderId = senderId,
        senderName = senderName,
        senderAvatar = senderAvatar,
        text = text,
        messageType = try { MessageType.valueOf(messageType) } catch (e: Exception) { MessageType.TEXT },
        mediaUrl = mediaUrl,
        durationSeconds = durationSeconds,
        locationLat = locationLat,
        locationLng = locationLng,
        contactName = contactName,
        contactPhone = contactPhone,
        timestamp = timestamp,
        isSentByMe = isSentByMe,
        status = try { MessageStatus.valueOf(status) } catch (e: Exception) { MessageStatus.DELIVERED },
        isEncrypted = isEncrypted,
        reactionEmoji = reactionEmoji
    )

    private fun ChatEntity.toDomain() = Chat(
        id = id,
        name = name,
        avatarUrl = avatarUrl,
        isGroup = isGroup,
        isCommunity = isCommunity,
        unreadCount = unreadCount,
        lastMessageText = lastMessageText,
        lastMessageTime = lastMessageTime,
        isOnline = isOnline,
        isPinned = isPinned,
        isArchived = isArchived,
        isMuted = isMuted,
        memberCount = memberCount
    )

    private fun CallEntity.toDomain() = CallLog(
        id = id,
        participantId = participantId,
        participantName = participantName,
        participantAvatar = participantAvatar,
        isVideoCall = isVideoCall,
        callStatus = try { CallStatus.valueOf(callStatus) } catch (e: Exception) { CallStatus.CONNECTED },
        timestamp = timestamp,
        durationSeconds = durationSeconds
    )

    private fun StatusEntity.toDomain() = UserStatus(
        id = id,
        userId = userId,
        userName = userName,
        userAvatar = userAvatar,
        mediaUrl = mediaUrl,
        caption = caption,
        timestamp = timestamp,
        isViewed = isViewed
    )
}
