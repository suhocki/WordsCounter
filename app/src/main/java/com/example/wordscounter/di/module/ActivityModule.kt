package com.example.wordscounter.di.module

import com.example.wordscounter.WordsActivity
import com.example.wordscounter.WordsViewModel
import com.example.wordscounter.di.Module

class ActivityModule(
) : Module {
    private val wordsViewModel = WordsViewModel()

    val wordsActivity: WordsActivity
        get() = WordsActivity(wordsViewModel)
}