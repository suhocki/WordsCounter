package com.example.wordscounter.di.module

import com.example.wordscounter.WordsCounterActivity
import com.example.wordscounter.WordsCounterViewModel
import com.example.wordscounter.di.Module

class ActivityModule(
) : Module {
    private val wordsCounterViewModel = WordsCounterViewModel()

    val wordsCounterActivity: WordsCounterActivity
        get() = WordsCounterActivity(wordsCounterViewModel)
}