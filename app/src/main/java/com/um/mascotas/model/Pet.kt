package com.um.mascotas.model

import com.um.mascotas.model.enums.Gender
import com.um.mascotas.model.enums.Hair
import com.um.mascotas.model.enums.Species

data class Pet(
    val id: String = "",
    val ownerId: String = "",
    val name: String = "",
    val species: Species = Species.DOG,
    val breed: String = "",
    val age: String = "",
    val gender: Gender = Gender.MALE,
    val hair: Hair = Hair.MEDIUM,
    val description: String = "",
    val photoUrl: String = "",
    val isAdopted: Boolean = false,
    val adopterId: String? = null,
)