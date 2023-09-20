package com.example.wordscounter.ui

import android.content.res.Resources
import com.example.wordscounter.R
import com.example.wordscounter.TestCoroutineRule
import com.example.wordscounter.domain.Book
import com.example.wordscounter.domain.BooksReader
import com.example.wordscounter.domain.Sort
import com.example.wordscounter.domain.WordFrequency
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
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

    private val message = "frequency"
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
        every { resources.getString(R.string.sorted_by_frequency) }.returns(message)
    }

    @Test
    fun `get words`() = runTest {
        assertEquals(
            words.toSet(),
            viewModel.getWordsFrequency().firstOrNull()?.toSet(),
        )
    }

    @Test
    fun `initial sort type is by frequency`() = runTest {
        assertEquals(Sort.FREQUENCY, viewModel.getSortType().firstOrNull())
    }

    @Test
    fun `sort toggle`() = runTest {
        val remainingSortTypes = Sort.values().toMutableSet()

        repeat(remainingSortTypes.size) {
            viewModel.changeSort()
            remainingSortTypes.remove(viewModel.getSortType().firstOrNull())
        }

        assertTrue(remainingSortTypes.isEmpty())
        assertEquals(Sort.FREQUENCY, viewModel.getSortType().firstOrNull())
    }

    @Test
    fun `sort by frequency`() = runTest {
        viewModel.sortBy(Sort.FREQUENCY)

        assertEquals(
            words.sortedByDescending { it.count },
            viewModel.getWordsFrequency().firstOrNull(),
        )
    }

    @Test
    fun `sort alphabetically`() = runTest {
        viewModel.sortBy(Sort.ALPHABETICALLY)

        assertEquals(
            words.sortedBy { it.word },
            viewModel.getWordsFrequency().firstOrNull(),
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

    @Test
    fun `progress shown while sorting`() = runTest {
        viewModel.changeSort()

        assertTrue(requireNotNull(viewModel.isProgress().firstOrNull()))
    }

    @Test
    fun `progress hidden on animation complete`() = runTest {
        viewModel.changeSort()
        viewModel.onListAnimationCompleted()

        assertFalse(requireNotNull(viewModel.isProgress().firstOrNull()))
    }

    @Test
    fun `message shown on animation complete`() = runTest {
        viewModel.onListAnimationCompleted()

        assertEquals(message, viewModel.getInfoMessage().firstOrNull())
    }

    private suspend fun WordsFrequencyViewModel.sortBy(
        newSortType: Sort,
    ) {
        do {
            changeSort()
        } while (getSortType().firstOrNull() != newSortType)
    }
}
