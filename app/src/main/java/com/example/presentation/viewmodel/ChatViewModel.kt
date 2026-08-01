package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.SilaRepository
import com.example.domain.model.Chat
import com.example.domain.model.ChatMessage
import com.example.domain.model.MessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: SilaRepository
) : ViewModel() {

    val chats: StateFlow<List<Chat>> = repository.chats
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedTab = MutableStateFlow(0) // 0: الكل, 1: غير مقروءة, 2: المجموعات, 3: المجتمعات
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    val filteredChats: StateFlow<List<Chat>> = combine(chats, _searchQuery, _selectedTab) { list, query, tab ->
        var filtered = list
        if (query.isNotBlank()) {
            filtered = filtered.filter { it.name.contains(query, ignoreCase = true) || it.lastMessageText.contains(query, ignoreCase = true) }
        }
        when (tab) {
            1 -> filtered.filter { it.unreadCount > 0 }
            2 -> filtered.filter { it.isGroup }
            3 -> filtered.filter { it.isCommunity }
            else -> filtered
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun getMessages(chatId: String): Flow<List<ChatMessage>> {
        return repository.getMessages(chatId)
    }

    fun markAsRead(chatId: String) {
        viewModelScope.launch {
            repository.clearUnread(chatId)
        }
    }

    fun sendMessage(
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
        viewModelScope.launch {
            repository.sendMessage(
                chatId = chatId,
                text = text,
                type = type,
                mediaUrl = mediaUrl,
                durationSeconds = durationSeconds,
                locationLat = locationLat,
                locationLng = locationLng,
                contactName = contactName,
                contactPhone = contactPhone
            )
        }
    }

    fun addReaction(messageId: String, emoji: String) {
        viewModelScope.launch {
            repository.updateReaction(messageId, emoji)
        }
    }

    fun createGroup(name: String, description: String, selectedContacts: List<String>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.createNewGroup(name, description, selectedContacts)
            onSuccess()
        }
    }
}
