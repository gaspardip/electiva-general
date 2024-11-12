package com.um.mascotas.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SnackbarManager {
    private val _messages = MutableSharedFlow<SnackbarEvent>()
    val messages = _messages.asSharedFlow()

    suspend fun showMessage(message: String) {
        _messages.emit(SnackbarEvent.Message(message))
    }

    suspend fun showError(message: String) {
        _messages.emit(SnackbarEvent.Error(message))
    }
}

sealed class SnackbarEvent {
    data class Message(val message: String) : SnackbarEvent()
    data class Error(val message: String) : SnackbarEvent()
}
