package com.um.mascotas.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.um.mascotas.model.User
import com.um.mascotas.model.enums.UserType
import com.um.mascotas.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    val user = auth.currentUser

    suspend fun saveUserProfile(
        userType: UserType,
        organizationName: String?,
        phoneNumber: String,
        address: String?,
        onSuccess: () -> Unit
    ) {
        val userProfile = User(
            id = user?.uid ?: "",
            displayName = user?.displayName ?: "",
            email = user?.email ?: "",
            userType = userType,
            organizationName = organizationName,
            phoneNumber = phoneNumber,
            address = address
        )

        userRepository.saveUser(userProfile)

        onSuccess()
    }
}
