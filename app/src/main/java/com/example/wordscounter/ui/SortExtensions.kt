package com.example.wordscounter.ui

import androidx.annotation.IdRes
import com.example.wordscounter.R
import com.example.wordscounter.domain.Sort

@get:IdRes
val Sort.menuId: Int
    get() = when (this) {
        Sort.ALPHABETICALLY -> R.id.sort_alphabetically
        Sort.CHAR_LENGTH -> R.id.sort_by_char_length
        Sort.FREQUENCY -> R.id.sort_by_frequency
    }