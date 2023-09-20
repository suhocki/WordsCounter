package com.example.wordscounter.ui

import android.content.res.Resources
import com.example.wordscounter.TestCoroutineRule
import com.example.wordscounter.domain.Book
import com.example.wordscounter.domain.BooksReader
import com.example.wordscounter.domain.Sort
import com.example.wordscounter.domain.WordFrequency
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WordsFrequencyViewModelTest {

    @Rule
    @JvmField
    val coroutineRule = TestCoroutineRule()

    private val booksReader: BooksReader = mockk(relaxed = true)
    private val resources: Resources = mockk(relaxed = true)

    private val words = (0..10)
        .map { i -> 'a' + i }
        .flatMap { char -> listOf("$char", "$char$char", "$char${char + 1}") }
        .mapIndexed { index, word -> WordFrequency(word, index) }
        .shuffled()

    private val viewModel by lazy {
        WordsFrequencyViewModel(booksReader, resources, coroutineRule.coroutineDispatchers)
    }

    @Before
    fun setUp() {
        coEvery { booksReader.getWordsFrequency(Book.ROMEO_AND_JULIET) }.returns(words)
    }

    @Test
    fun `sort by frequency`() = runTest {
        viewModel.sortBy(Sort.FREQUENCY)

        assertEquals(
            viewModel.getWordsFrequency().firstOrNull(),
            words.sortedByDescending { it.count },
        )
    }

    @Test
    fun `sort alphabetically`() = runTest {
        viewModel.sortBy(Sort.ALPHABETICALLY)

        assertEquals(
            viewModel.getWordsFrequency().firstOrNull(),
            words.sortedBy { it.word },
        )
    }

    @Test
    fun `sort by char length`() = runTest {
        viewModel.sortBy(Sort.CHAR_LENGTH)

        assertEquals(
            words
                .sortedWith { o1, o2 -> o1.word.length.compareTo(o2.word.length) }
                .groupBy { it.word.length }
                .toSortedMap { o1, o2 -> o2.compareTo(o1) }
                .flatMap { (_, value) -> value.sortedBy(WordFrequency::word) },
            viewModel.getWordsFrequency().firstOrNull(),
        )
    }

    private suspend fun WordsFrequencyViewModel.sortBy(
        newSortType: Sort,
    ) {
        do {
            changeSort()
        } while (getSortType().firstOrNull() != newSortType)
    }
}
