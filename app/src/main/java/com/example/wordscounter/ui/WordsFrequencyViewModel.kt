package com.example.wordscounter.ui

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordscounter.domain.Book
import com.example.wordscounter.domain.BooksReader
import com.example.wordscounter.domain.Sort
import com.example.wordscounter.domain.WordFrequency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WordsFrequencyViewModel(
    private val booksReader: BooksReader,
    private val resources: Resources,
) : ViewModel() {
    private val sortTypeQueue = ArrayDeque(Sort.values().toList())

    private val sortType = MutableStateFlow<Sort?>(null)
    private val isProgress = MutableStateFlow(false)
    private val infoMessage = MutableSharedFlow<String>()
    private val wordsFrequency = MutableSharedFlow<List<WordFrequency>>(1, 1)

    init {
        changeSort()
    }

    fun changeSort() {
        viewModelScope.launch(Dispatchers.Default) {
            isProgress.emit(true)
            sortType.emit(null)

            with(sortTypeQueue) { addLast(removeFirst()) }

            val newSortType = sortTypeQueue.first()
            val frequency = booksReader.getWordsFrequency(Book.ROMEO_AND_JULIET)
                .sortedWith(newSortType.comparator)

            wordsFrequency.emit(frequency)
            sortType.emit(newSortType)
            infoMessage.emit(resources.getString(newSortType.titleRes))
        }
    }

    fun getWordsFrequency(): Flow<List<WordFrequency>> = wordsFrequency

    fun getSortType(): Flow<Sort?> = sortType

    fun isProgress(): Flow<Boolean> = isProgress

    fun onListAnimationCompleted() {
        isProgress.tryEmit(false)
    }

    fun getInfoMessage(): Flow<String> = infoMessage
}