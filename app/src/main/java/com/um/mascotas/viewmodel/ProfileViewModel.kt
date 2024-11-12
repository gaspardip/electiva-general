package com.um.mascotas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.um.mascotas.model.User
import com.um.mascotas.model.enums.UserType
import com.um.mascotas.repository.UserRepository
import com.um.mascotas.utils.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> get() = _isSaving

    fun getUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            try {


                _user.value = userRepository.fetchCurrentUser()
            } catch (e: Exception) {
                _user.value = null

                SnackbarManager.showError("Error al cargar el perfil: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateUserProfile(
        displayName: String,
        organizationName: String?,
        phoneNumber: String,
        address: String?
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                val uid = userRepository.currentUser.value!!.id;
                val userType = userRepository.currentUser.value!!.userType;
                val email = _user.value?.email ?: ""

                val updatedUser = User(
                    id = uid,
                    displayName = displayName,
                    email = email,
                    userType = userType,
                    organizationName = if (userType == UserType.DONOR) organizationName else null,
                    phoneNumber = phoneNumber,
                    address = address
                )

                userRepository.saveUser(updatedUser)

                _user.value = updatedUser

                SnackbarManager.showMessage("Perfil actualizado exitosamente")
            } catch (e: Exception) {
                SnackbarManager.showError("Error al guardar el perfil: ${e.message}")
            } finally {
                _isSaving.value = false
            }
        }
    }
}
