package com.um.mascotas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.um.mascotas.messaging.MessagingManager
import com.um.mascotas.model.User
import com.um.mascotas.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val messagingManager: MessagingManager
) : ViewModel() {

    val currentUser: StateFlow<User?> get() = userRepository.currentUser
    val isLoadingUser: StateFlow<Boolean> get() = userRepository.isLoadingUser

    init {
        // Fetch current user initially
        viewModelScope.launch {
            userRepository.fetchCurrentUser()
        }

        // Observe `currentUser` changes to manage FCM topic subscription
        viewModelScope.launch {
            userRepository.currentUser.collectLatest { user ->
                if (user != null) {
                    // Subscribe to FCM topic when the user is logged in
                    messagingManager.subscribeToPetsTopic()
                } else {
                    // Unsubscribe when the user logs out
                    messagingManager.unsubscribeFromPetsTopic()
                }
            }
        }
    }

    fun signOut() {
        userRepository.signOut()
    }
}
