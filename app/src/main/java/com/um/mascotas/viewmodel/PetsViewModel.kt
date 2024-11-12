// PetsViewModel.kt
package com.um.mascotas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.um.mascotas.model.Pet
import com.um.mascotas.model.PetFilter
import com.um.mascotas.model.User
import com.um.mascotas.model.enums.Gender
import com.um.mascotas.model.enums.Hair
import com.um.mascotas.model.enums.Species
import com.um.mascotas.model.isDonor
import com.um.mascotas.repository.PetsRepository
import com.um.mascotas.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PetsViewModel @Inject constructor(
    private val petsRepository: PetsRepository,
    userRepository: UserRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = userRepository.currentUser

    private val _selectedSpecies = MutableStateFlow<Species?>(null)
    val selectedSpecies: StateFlow<Species?> = _selectedSpecies.asStateFlow()

    private val _selectedBreed = MutableStateFlow<String?>(null)
    val selectedBreed: StateFlow<String?> = _selectedBreed.asStateFlow()

    private val _selectedGender = MutableStateFlow<Gender?>(null)
    val selectedGender: StateFlow<Gender?> = _selectedGender.asStateFlow()

    private val _selectedHair = MutableStateFlow<Hair?>(null)
    val selectedHair: StateFlow<Hair?> = _selectedHair.asStateFlow()

    private val _selectedAgeRange = MutableStateFlow<IntRange?>(null)
    val selectedAgeRange: StateFlow<IntRange?> = _selectedAgeRange.asStateFlow()

    private val initialOnlyOwnPets = userRepository.currentUser.value?.isDonor() == true

    private val _onlyOwnPets = MutableStateFlow(initialOnlyOwnPets)
    val onlyOwnPets: StateFlow<Boolean> = _onlyOwnPets.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    private val filtersFlow = combine(
        _onlyOwnPets,
        _selectedSpecies,
        _selectedBreed,
        _selectedGender,
        _selectedHair,
//        _selectedAgeRange
    ) { onlyOwnPets, species, breed, gender, hair ->
        PetFilter(
            species = species,
            breed = breed,
            gender = gender,
            hair = hair,
//            minAge = ageRange?.first,
//            maxAge = ageRange?.last,
            onlyOwnPets = onlyOwnPets
        )
    }

    // Pets flow that fetches data whenever filters or onlyOwnPets change
    val pets: StateFlow<List<Pet>> = filtersFlow
        .flatMapLatest { filters ->
            flow {
                _isLoading.value = true
                try {
                    val petsList = petsRepository.fetchPets(filters)
                    emit(petsList)
                } catch (e: Exception) {
                    e.printStackTrace()
                    emit(emptyList())
                } finally {
                    _isLoading.value = false
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    // Functions to update filters
    fun setSpecies(species: Species?) {
        _selectedSpecies.value = species
    }

    fun setBreed(breed: String?) {
        _selectedBreed.value = breed
    }

    fun setGender(gender: Gender?) {
        _selectedGender.value = gender
    }

    fun setHair(hair: Hair?) {
        _selectedHair.value = hair
    }

    fun setAgeRange(ageRange: IntRange?) {
        _selectedAgeRange.value = ageRange
    }

    fun setOnlyOwnPets(onlyOwnPets: Boolean) {
        _onlyOwnPets.value = onlyOwnPets
    }

    fun clearFilters() {
        _onlyOwnPets.value = initialOnlyOwnPets
        _selectedSpecies.value = null
        _selectedBreed.value = null
        _selectedGender.value = null
        _selectedHair.value = null
        _selectedAgeRange.value = null
    }
}
