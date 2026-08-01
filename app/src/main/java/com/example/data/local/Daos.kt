package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM chat_messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessagesForChat(chatId: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ChatMessageEntity>)

    @Query("DELETE FROM chat_messages WHERE id = :messageId")
    suspend fun deleteMessage(messageId: String)

    @Query("UPDATE chat_messages SET reactionEmoji = :reaction WHERE id = :messageId")
    suspend fun updateReaction(messageId: String, reaction: String)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats ORDER BY isPinned DESC, lastMessageTime DESC")
    fun getAllChats(): Flow<List<ChatEntity>>

    @Query("SELECT * FROM chats WHERE id = :chatId")
    suspend fun getChatById(chatId: String): ChatEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChats(chats: List<ChatEntity>)

    @Query("UPDATE chats SET lastMessageText = :lastText, lastMessageTime = :time WHERE id = :chatId")
    suspend fun updateLastMessage(chatId: String, lastText: String, time: Long)

    @Query("UPDATE chats SET unreadCount = 0 WHERE id = :chatId")
    suspend fun clearUnreadCount(chatId: String)
}

@Dao
interface CallDao {
    @Query("SELECT * FROM call_logs ORDER BY timestamp DESC")
    fun getAllCalls(): Flow<List<CallEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCall(call: CallEntity)
}

@Dao
interface StatusDao {
    @Query("SELECT * FROM statuses ORDER BY timestamp DESC")
    fun getAllStatuses(): Flow<List<StatusEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatus(status: StatusEntity)

    @Query("UPDATE statuses SET isViewed = 1 WHERE id = :statusId")
    suspend fun markAsViewed(statusId: String)
}
