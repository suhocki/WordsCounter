package com.example.wordscounter.di.module

import android.content.Context
import android.content.res.Resources
import com.example.wordscounter.data.AssetsRepository
import com.example.wordscounter.di.Module
import com.example.wordscounter.di.scope.RootScope
import com.example.wordscounter.domain.BooksReader
import com.example.wordscounter.domain.CharsetDetector
import com.example.wordscounter.domain.CoroutineDispatchers
import com.example.wordscounter.domain.FilesRepository
import com.example.wordscounter.domain.WordsFrequencyCalculator
import com.example.wordscounter.ui.WordsFrequencyActivity
import com.example.wordscounter.ui.WordsFrequencyViewModel
import java.nio.charset.Charset

class ActivityModule(
    private val context: Context,
    private val coroutineDispatchers: CoroutineDispatchers,
) : Module {
    private val resources: Resources
        get() = context.resources

    private val filesRepository: FilesRepository
        get() = AssetsRepository(resources.assets, context.cacheDir)

    private val charsetDetector: CharsetDetector
        get() = CharsetDetector(Charset.availableCharsets())

    private val wordsFrequencyCalculator: WordsFrequencyCalculator
        get() = WordsFrequencyCalculator()

    private val booksReader: BooksReader
        get() = BooksReader(
            filesRepository = filesRepository,
            charsetDetector = charsetDetector,
            wordsFrequencyCalculator = wordsFrequencyCalculator,
            coroutineDispatchers = coroutineDispatchers,
        )

    private val wordsFrequencyViewModel = WordsFrequencyViewModel(
        booksReader = booksReader,
        resources = resources,
        coroutineDispatchers = coroutineDispatchers,
    ).apply {
        addCloseable(RootScope.scopes.activity::clear)
    }

    val wordsFrequencyActivity: WordsFrequencyActivity
        get() = WordsFrequencyActivity(wordsFrequencyViewModel)
}