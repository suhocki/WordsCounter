package com.example.wordscounter

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class WordsViewModel(
    private val filesRepository: FileRepository,
) {

    val words: Flow<List<Word>> = MutableStateFlow((0..100)
        .map { Word("word #$it", it) })

    init {
        // open file
    }
}