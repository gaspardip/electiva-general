package com.um.mascotas.model.enums

import androidx.annotation.StringRes
import com.um.mascotas.R

enum class Hair(@StringRes override val displayNameResId: Int) : DisplayableOption {
    SHORT(R.string.hair_short),
    MEDIUM(R.string.hair_medium),
    LONG(R.string.hair_long),
}