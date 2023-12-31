package com.example.wordscounter.domain

import com.example.wordscounter.domain.model.Book
import com.example.wordscounter.domain.model.WordFrequency
import java.io.InputStreamReader
import kotlinx.coroutines.withContext

class BooksReader(
    private val filesRepository: FilesRepository,
    private val charsetDetector: CharsetDetector,
    private val wordsFrequencyCalculator: WordsFrequencyCalculator,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val wordsFrequencyCache: MutableMap<Book, List<WordFrequency>> = mutableMapOf(),
    private val knownChars: Regex = "^[0-9A-z[:punct:]’\\s]+\$".toRegex()
) {
    suspend fun getWordsFrequency(
        book: Book,
        splitWords: Regex = "[\\p{L}’-]+".toRegex(),
    ): List<WordFrequency> = withContext(coroutineDispatchers.default) {
        wordsFrequencyCache[book]?.let { cachedWordFrequency ->
            return@withContext cachedWordFrequency
        }

        val file = filesRepository.getFile(book.fileName)
        val charset = charsetDetector.detectFileCharset(file, knownChars)

        InputStreamReader(file.inputStream(), charset)
            .readLines()
            .map { line -> splitWords.findAll(line).map(MatchResult::value).toList() }
            .forEach(wordsFrequencyCalculator::addWords)

        wordsFrequencyCalculator.getWordsFrequency().also { wordsFrequency ->
            wordsFrequencyCache[book] = wordsFrequency
        }
    }
}