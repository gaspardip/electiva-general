package com.um.mascotas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.um.mascotas.model.Pet
import com.um.mascotas.model.User
import com.um.mascotas.repository.PetsRepository
import com.um.mascotas.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val petsRepository: PetsRepository
) : ViewModel() {
    private val _pet = MutableStateFlow<Pet?>(null)
    val pet: StateFlow<Pet?> get() = _pet

    private val _donor = MutableStateFlow<User?>(null)
    val donor: StateFlow<User?> get() = _donor

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun fetchPetDetails(petId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val pet = petsRepository.fetchPetById(petId);
                _pet.value = pet

                // Fetch donor details
                pet?.let { pet ->
                    _donor.value = userRepository.fetchUserById(pet.ownerId)
                }
            } catch (e: Exception) {
                // Handle error
                _pet.value = null
                _donor.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}
