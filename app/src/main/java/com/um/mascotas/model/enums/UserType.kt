package com.um.mascotas.model.enums

import androidx.annotation.StringRes
import com.um.mascotas.R

enum class UserType(@StringRes val displayNameResId: Int) {
    DONOR(R.string.user_type_donor),
    ADOPTER(R.string.user_type_adopter),
}
