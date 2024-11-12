package com.um.mascotas.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.um.mascotas.model.Pet
import com.um.mascotas.model.enums.Gender
import com.um.mascotas.model.enums.Hair
import com.um.mascotas.model.enums.Species
import com.um.mascotas.repository.BreedRepository
import com.um.mascotas.repository.PetsRepository
import com.um.mascotas.repository.StorageRepository
import com.um.mascotas.utils.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val petsRepository: PetsRepository,
    private val storageRepository: StorageRepository,
    private val breedRepository: BreedRepository,
) : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Estado del formulario
    private val _formState = MutableStateFlow(AddPetForm())
    val formState: StateFlow<AddPetForm> = _formState.asStateFlow()

    private val _breeds = MutableStateFlow<List<String>>(emptyList())
    val breeds: StateFlow<List<String>> get() = _breeds

    val isFormValid: StateFlow<Boolean> = formState.map { form ->
        form.name.isNotBlank() &&
                form.breed.isNotBlank() &&
                form.age.isNotBlank() &&
                form.description.isNotBlank() &&
                form.photoUri != null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // Estado de carga
    private val _isUploading = MutableStateFlow(false)
    val isUploading = _isUploading.asStateFlow()

    // Funciones para actualizar cada campo del formulario
    fun updateName(newName: String) {
        _formState.value = _formState.value.copy(name = newName)
    }

    fun updateSpecies(newSpecies: Species) {
        _formState.value = _formState.value.copy(species = newSpecies)
    }

    fun updateBreed(newBreed: String) {
        _formState.value = _formState.value.copy(breed = newBreed)
    }

    fun updateAge(newAge: String) {
        _formState.value = _formState.value.copy(age = newAge)
    }

    fun updateGender(newGender: Gender) {
        _formState.value = _formState.value.copy(gender = newGender)
    }

    fun updateHair(newHair: Hair) {
        _formState.value = _formState.value.copy(hair = newHair)
    }

    fun updateDescription(newDescription: String) {
        _formState.value = _formState.value.copy(description = newDescription)
    }

    fun updatePhotoUri(newPhotoUri: Uri?) {
        _formState.value = _formState.value.copy(photoUri = newPhotoUri)
    }

    // Función para subir la foto y agregar la mascota
    suspend fun addPet() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            SnackbarManager.showError("Usuario no autenticado")
            return
        }

        viewModelScope.launch {
            _isUploading.value = true
            try {
                val photoUrl = uploadPetPhoto(_formState.value.photoUri)
                if (photoUrl != null) {
                    val pet = Pet(
                        ownerId = uid,
                        name = _formState.value.name,
                        species = _formState.value.species,
                        breed = _formState.value.breed,
                        age = _formState.value.age,
                        gender = _formState.value.gender,
                        hair = _formState.value.hair,
                        description = _formState.value.description,
                        photoUrl = photoUrl
                    )
                    petsRepository.addPet(pet)
                    SnackbarManager.showMessage("Animal registrado exitosamente")
                    // Reiniciar el formulario después de agregar la mascota
                    _formState.value = AddPetForm()
                } else {
                    SnackbarManager.showError("Error al obtener la URL de la foto")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                SnackbarManager.showError("Error al registrar el animal")
            } finally {
                _isUploading.value = false
            }
        }
    }

    // Función para subir la foto
    private suspend fun uploadPetPhoto(uri: Uri?): String? {
        if (uri == null) {
            SnackbarManager.showError("Por favor selecciona una foto")
            return null
        }

        return try {
            storageRepository.uploadPetPhoto(uri)
        } catch (e: Exception) {
            e.printStackTrace()
            SnackbarManager.showError("Error al subir la foto")
            null
        }
    }

    fun fetchBreeds(species: Species) {
        viewModelScope.launch {
            _breeds.value = breedRepository.getBreedsBySpecies(species)
        }
    }
}

data class AddPetForm(
    val name: String = "",
    val species: Species = Species.DOG,
    val breed: String = "",
    val age: String = "",
    val gender: Gender = Gender.MALE,
    val hair: Hair = Hair.MEDIUM,
    val description: String = "",
    val photoUri: Uri? = null,
)