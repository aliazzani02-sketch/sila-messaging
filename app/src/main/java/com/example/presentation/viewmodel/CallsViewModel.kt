package com.example.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.SilaRepository
import com.example.domain.model.CallLog
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CallsViewModel(
    private val repository: SilaRepository
) : ViewModel() {

    val callLogs: StateFlow<List<CallLog>> = repository.calls
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun startCall(participantName: String, isVideo: Boolean) {
        viewModelScope.launch {
            repository.addCallRecord(participantName, isVideo)
        }
    }
}
