package com.um.mascotas.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.um.mascotas.model.Pet
import com.um.mascotas.model.PetFilter
import com.um.mascotas.model.enums.Gender
import com.um.mascotas.model.enums.Species
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetsRepository @Inject constructor(
    db: FirebaseFirestore,
    private val userRepository: UserRepository
) {
    private val collection = db.collection("pets")

    suspend fun fetchPets(petFilter: PetFilter? = null): List<Pet> {
        return try {
            var query = collection.limit(50)

            petFilter?.let { filter ->
                filter.onlyOwnPets.let {
                    if (!it) {
                        return@let
                    }

                    val id = userRepository.currentUser.value?.id

                    query = query.whereEqualTo("ownerId", id)
                }
                filter.species?.let {
                    query = query.whereEqualTo("species", it.name)
                }
                filter.gender?.let {
                    query = query.whereEqualTo("gender", it.name)
                }
                filter.hair?.let {
                    query = query.whereEqualTo("hair", it.name)
                }
                filter.breed?.let {
                    query = query.whereEqualTo("breed", it)
                }
//
//            if (filter.minAge != null && filter.maxAge != null) {
//                query = query.whereGreaterThanOrEqualTo("age", filter.minAge)
//                    .whereLessThanOrEqualTo("age", filter.maxAge)
//            } else if (filter.minAge != null) {
//                query = query.whereGreaterThanOrEqualTo("age", filter.minAge)
//            } else if (filter.maxAge != null) {
//                query = query.whereLessThanOrEqualTo("age", filter.maxAge)
//            }
            }

            val snapshot = query.get().await()

            snapshot.documents.mapNotNull { it.toPet() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun fetchPetById(petId: String): Pet? {
        val snapshot = collection.document(petId).get().await()
        return snapshot.toPet()
    }

    suspend fun addPet(pet: Pet) {
        val documentId = collection.document().id
        val petWithId = pet.copy(id = documentId)

        collection.document(petWithId.id).set(petWithId.toMap()).await()
    }

    suspend fun savePet(pet: Pet) {
        collection.document(pet.id).set(pet.toMap()).await()
    }
}

fun DocumentSnapshot.toPet(): Pet? {
    val speciesString = getString("species") ?: Species.DOG.name
    val genderString = getString("gender") ?: Gender.MALE.name

    return this.toObject(Pet::class.java)?.copy(
        species = Species.valueOf(speciesString),
        gender = Gender.valueOf(genderString)
    )
}

fun Pet.toMap(): Map<String, Any?> {
    return mapOf(
        "id" to id,
        "ownerId" to ownerId,
        "name" to name,
        "species" to species.name,
        "breed" to breed,
        "age" to age,
        "gender" to gender.name,
        "hair" to hair.name,
        "description" to description,
        "photoUrl" to photoUrl,
        "isAdopted" to isAdopted,
        "adopterId" to adopterId
    )
}