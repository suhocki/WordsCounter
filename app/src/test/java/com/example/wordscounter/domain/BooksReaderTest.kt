package com.example.wordscounter.domain

import com.example.wordscounter.domain.model.Book
import com.example.wordscounter.domain.model.WordFrequency
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File

class BooksReaderTest {
    private val repository: FilesRepository = mockk()
    private val charsetDetector: CharsetDetector = mockk()
    private val calculator: WordsFrequencyCalculator = mockk(relaxed = true)
    private val wordsFrequency: List<WordFrequency> = listOf(mockk())

    private val cache = mutableMapOf<Book, List<WordFrequency>>()
    private val book = Book.ROMEO_AND_JULIET
    private val file = File(requireNotNull(javaClass.classLoader).getResource("utf-16.txt").file)

    private val reader = BooksReader(
        filesRepository = repository,
        charsetDetector = charsetDetector,
        wordsFrequencyCalculator = calculator,
        wordsFrequencyCache = cache,
        knownChars = "^[a-z\\s]+\$".toRegex()
    )

    @Before
    fun setUp() {
        coEvery { repository.getFile(book.fileName) } coAnswers { file }
        coEvery { charsetDetector.detectFileCharset(file, any()) } coAnswers { Charsets.UTF_16 }
    }

    @Test
    fun `get from cache`() = runTest {
        cache[book] = wordsFrequency

        assertEquals(cache[book], reader.getWordsFrequency(book))
        verify(exactly = 0) { calculator.getWordsFrequency() }
    }

    @Test
    fun `add words`() = runTest {
        reader.getWordsFrequency(book, "[a-z]+".toRegex())

        verify { calculator.addWords(listOf("utf", "sixteen", "here")) }
    }

    @Test
    fun `get words frequency`() = runTest {
        every { calculator.getWordsFrequency() } answers { wordsFrequency }

        assertEquals(wordsFrequency, reader.getWordsFrequency(book))
    }

    @Test
    fun `cache results`() = runTest {
        every { calculator.getWordsFrequency() } answers { wordsFrequency }

        repeat(10) { reader.getWordsFrequency(book) }

        verify(exactly = 1) { calculator.getWordsFrequency() }
    }
}