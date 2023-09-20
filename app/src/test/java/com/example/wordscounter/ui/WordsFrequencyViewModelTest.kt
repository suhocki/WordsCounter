package com.example.wordscounter.ui

import android.content.res.Resources
import com.example.wordscounter.domain.Book
import com.example.wordscounter.domain.BooksReader
import com.example.wordscounter.domain.WordFrequency
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class WordsFrequencyViewModelTest {
    private val booksReader: BooksReader = mockk(relaxed = true)
    private val resources: Resources = mockk(relaxed = true)

    private val viewModel by lazy { WordsFrequencyViewModel(booksReader, resources) }

    @Test
    fun `sort by frequency`() = runTest {
        val words = (0..10).map { count -> WordFrequency("word", count) }

        coEvery { booksReader.getWordsFrequency(Book.ROMEO_AND_JULIET) }.returns(words)

        assertEquals(
            viewModel.getWordsFrequency().firstOrNull().orEmpty(),
            words.sortedByDescending { it.count },
        )
    }
}
