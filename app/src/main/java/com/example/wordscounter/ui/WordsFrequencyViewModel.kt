package com.example.wordscounter.ui

import com.example.wordscounter.domain.Book
import com.example.wordscounter.domain.BooksReader
import com.example.wordscounter.domain.Sort
import com.example.wordscounter.domain.WordFrequency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WordsFrequencyViewModel(
    private val booksReader: BooksReader,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val book = Book.ROMEO_AND_JULIET
    private val wordsFrequency = MutableSharedFlow<List<WordFrequency>>(1, 1)

    private val sortType = MutableStateFlow<Sort?>(null)
    private val sortTypeQueue = ArrayDeque(Sort.values().toList())

    private val isProgress = MutableStateFlow(false)

    init {
        changeSort()
    }

    fun changeSort() {
        coroutineScope.launch(Dispatchers.Default) {
            isProgress.emit(true)
            sortType.emit(null)

            with(sortTypeQueue) { addLast(removeFirst()) }

            val frequency = booksReader.getWordsFrequency(book)
                .sortedWith(sortTypeQueue.first().comparator)

            wordsFrequency.emit(frequency)
            sortType.emit(sortTypeQueue.first())
        }
    }

    fun getWordsFrequency(): Flow<List<WordFrequency>> = wordsFrequency

    fun getSortType(): Flow<Sort?> = sortType

    fun isProgress(): Flow<Boolean> = isProgress
    fun onListAnimationCompleted() {
        isProgress.tryEmit(false)
    }
}