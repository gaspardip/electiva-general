package com.um.mascotas.repository

import android.util.Log
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.um.mascotas.model.enums.Species
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreedRepository @Inject constructor() {
    private val functions: FirebaseFunctions = Firebase.functions

    suspend fun getBreedsBySpecies(species: Species): List<String> {
        return try {
            val functionName = if (species == Species.CAT) "getCatBreeds" else "getDogBreeds"
            val result = functions
                .getHttpsCallable(functionName)
                .call()
                .await()

            val breeds = result.data as List<String>

            breeds
        } catch (e: Exception) {
            Log.e("FirebaseFunctions", "Error al obtener razas", e)
            emptyList()
        }
    }
}
