package com.example.wordscounter.domain

import java.io.File
import java.io.InputStreamReader
import java.nio.charset.Charset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.streams.asSequence

class CharsetDetector(
    private val availableCharsets: Map<String, Charset>,
    private val minimumBlockSize: Int = 512,
    private val linesCount: Int = 16,
) {
    suspend fun detectFileCharset(
        file: File,
        acceptableChars: Regex,
    ): Charset = withContext(Dispatchers.IO) {
        availableCharsets.values
            .associateWith { charset -> validLinesCount(file, charset, acceptableChars) }
            .maxBy { (_, validLines) -> validLines }
            .key
    }

    private fun validLinesCount(
        file: File,
        charset: Charset,
        acceptableChars: Regex
    ) = InputStreamReader(file.inputStream(), charset).use { reader ->
        reader
            .buffered(minimumBlockSize).lines().asSequence()
            .chunked(linesCount)
            .first()
            .count(acceptableChars::matches)
    }
}
