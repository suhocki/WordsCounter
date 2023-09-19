package com.example.wordscounter.domain

import java.io.InputStreamReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BooksReader(
    private val filesRepository: FilesRepository,
    private val charsetDetector: CharsetDetector,
    private val wordsFrequencyCalculator: WordsFrequencyCalculator,
    private val wordsFrequencyCache: MutableMap<Book, List<WordFrequency>> = mutableMapOf()
) {
    suspend fun getWordsFrequency(
        book: Book,
    ): List<WordFrequency> = withContext(Dispatchers.Default) {
        wordsFrequencyCache[book]?.let { cachedWordFrequency ->
            return@withContext cachedWordFrequency
        }

        val file = filesRepository.getFile(book.fileName)
        val charset = charsetDetector.detectFileCharset(file, KNOWN_CHARS)

        InputStreamReader(file.inputStream(), charset)
            .readLines()
            .map { line -> EXTRACT_WORDS.findAll(line).map(MatchResult::value).toList() }
            .forEach(wordsFrequencyCalculator::addWords)

        wordsFrequencyCalculator.getWordsFrequency().also { wordsFrequency ->
            wordsFrequencyCache[book] = wordsFrequency
        }
    }

    companion object {
        private val KNOWN_CHARS = "^[0-9[:punct:]\\s\\p{L}]+\$".toRegex()
        private val EXTRACT_WORDS = "[\\p{L}’-]+".toRegex()
    }
}