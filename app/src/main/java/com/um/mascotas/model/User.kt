package com.um.mascotas.model

import com.um.mascotas.model.enums.UserType

data class User(
    val id: String = "",
    val displayName: String = "",
    val email: String = "",
    val userType: UserType = UserType.ADOPTER,
    val organizationName: String? = null,
    val phoneNumber: String? = null,
    val address: String? = null
)

fun User.isDonor() = userType == UserType.DONOR
fun User.isAdopter() = userType == UserType.ADOPTER