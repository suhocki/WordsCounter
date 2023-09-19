package com.example.wordscounter.di.module

import android.content.Context
import android.content.res.AssetManager
import com.example.wordscounter.data.AssetsRepository
import com.example.wordscounter.di.Module
import com.example.wordscounter.domain.BooksReader
import com.example.wordscounter.domain.CharsetDetector
import com.example.wordscounter.domain.FilesRepository
import com.example.wordscounter.domain.WordsFrequencyCalculator
import com.example.wordscounter.ui.WordsFrequencyActivity
import com.example.wordscounter.ui.WordsFrequencyViewModel
import java.nio.charset.Charset

class ActivityModule(
    private val context: Context,
) : Module {
    private val assetManager: AssetManager
        get() = context.assets

    private val filesRepository: FilesRepository
        get() = AssetsRepository(assetManager, context)

    private val charsetDetector: CharsetDetector
        get() = CharsetDetector(Charset.availableCharsets())

    private val wordsFrequencyCalculator: WordsFrequencyCalculator
        get() = WordsFrequencyCalculator()

    private val booksReader: BooksReader
        get() = BooksReader(
            filesRepository = filesRepository,
            charsetDetector = charsetDetector,
            wordsFrequencyCalculator = wordsFrequencyCalculator,
        )

    private val wordsFrequencyViewModel = WordsFrequencyViewModel(
        booksReader = booksReader,
    )

    val wordsFrequencyActivity: WordsFrequencyActivity
        get() = WordsFrequencyActivity(wordsFrequencyViewModel)
}