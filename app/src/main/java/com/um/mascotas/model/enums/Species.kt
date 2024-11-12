package com.um.mascotas.model.enums

import androidx.annotation.StringRes
import com.um.mascotas.R

enum class Species(@StringRes override val displayNameResId: Int) : DisplayableOption {
    DOG(R.string.species_dog),
    CAT(R.string.species_cat),
}