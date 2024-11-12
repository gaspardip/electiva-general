package com.um.mascotas.model.enums

import androidx.annotation.StringRes
import com.um.mascotas.R

enum class Gender(@StringRes override val displayNameResId: Int) : DisplayableOption {
    MALE(R.string.gender_male),
    FEMALE(R.string.gender_female),
}