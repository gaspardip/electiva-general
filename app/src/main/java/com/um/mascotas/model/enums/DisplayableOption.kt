package com.um.mascotas.model.enums

import androidx.annotation.StringRes

interface DisplayableOption {
    @get:StringRes
    val displayNameResId: Int
}
