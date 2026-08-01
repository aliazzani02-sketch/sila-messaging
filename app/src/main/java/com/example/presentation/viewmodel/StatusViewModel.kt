package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.SilaRepository
import com.example.domain.model.UserStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StatusViewModel(
    private val repository: SilaRepository
) : ViewModel() {

    val statuses: StateFlow<List<UserStatus>> = repository.statuses
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun postStatus(caption: String, mediaUrl: String = "") {
        viewModelScope.launch {
            repository.postStatus(caption, mediaUrl)
        }
    }

    fun markViewed(statusId: String) {
        viewModelScope.launch {
            repository.markStatusViewed(statusId)
        }
    }
}
