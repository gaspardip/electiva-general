package com.um.mascotas.model

import com.um.mascotas.model.enums.Gender
import com.um.mascotas.model.enums.Hair
import com.um.mascotas.model.enums.Species

data class PetFilter(
    val species: Species? = null,
    val gender: Gender? = null,
    val hair: Hair? = null,
    val breed: String? = null,
    val minAge: Int? = null,
    val maxAge: Int? = null,
    val onlyOwnPets: Boolean = false
)