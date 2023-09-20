package com.example.wordscounter.ui

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.example.wordscounter.domain.BooksReader
import com.example.wordscounter.domain.CoroutineDispatchers
import com.example.wordscounter.domain.model.Book
import com.example.wordscounter.domain.model.Sort
import com.example.wordscounter.domain.model.WordFrequency
import com.example.wordscounter.ui.extension.titleRes
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope as baseViewModelScope

class WordsFrequencyViewModel(
    private val booksReader: BooksReader,
    private val resources: Resources,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ViewModel() {
    private val sortType = MutableStateFlow(Sort.FREQUENCY)
    private val isProgress = MutableStateFlow(false)
    private val infoMessage = MutableSharedFlow<String>(1)
    private val wordsFrequency = MutableSharedFlow<List<WordFrequency>>(1, 1)

    private val viewModelScope = CoroutineScope(
        baseViewModelScope.coroutineContext + CoroutineExceptionHandler { _, throwable ->
            infoMessage.tryEmit(throwable.localizedMessage.orEmpty())
        }
    )

    private var sortJob: Job? = null

    init {
        sortBy(Sort.FREQUENCY)
    }

    fun changeSort() = with(Sort.values()) {
        indexOf(sortType.value)
            .inc()
            .rem(size)
            .let(::get)
            .also(sortType::tryEmit)
            .let(::sortBy)
    }

    private fun sortBy(
        newSortType: Sort
    ) {
        sortJob?.cancel()
        sortJob = viewModelScope.launch(coroutineDispatchers.default) {
            isProgress.emit(true)

            booksReader
                .getWordsFrequency(Book.ROMEO_AND_JULIET)
                .sortedWith(newSortType.comparator)
                .let(wordsFrequency::tryEmit)

            sortType.emit(newSortType)
        }
    }

    fun onListAnimationCompleted() {
        isProgress.tryEmit(false)

        sortType.value.titleRes
            .let(resources::getString)
            .let(infoMessage::tryEmit)
    }

    fun getWordsFrequency(): Flow<List<WordFrequency>> = wordsFrequency

    fun getSortType(): Flow<Sort> = sortType

    fun isProgress(): Flow<Boolean> = isProgress

    fun getInfoMessage(): Flow<String> = infoMessage
}