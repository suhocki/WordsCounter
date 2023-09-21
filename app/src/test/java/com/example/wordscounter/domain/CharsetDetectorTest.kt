package com.example.wordscounter.domain

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.io.File
import java.nio.charset.Charset

@RunWith(Parameterized::class)
class CharsetDetectorTest(
    fileName: String,
    private val charset: Charset,
) {
    private val detector = CharsetDetector(
        Charset.availableCharsets(),
        linesCount = 1000,
    )

    private val file = File(requireNotNull(javaClass.classLoader).getResource(fileName).file)

    @Test
    fun `detect charset`() = runTest {
        assertEquals(
            charset,
            detector.detectFileCharset(file, "^[0-9A-z[:punct:]’\\s]+\$".toRegex()),
        )
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            preconditions(fileName = "utf-16.txt", charset = Charsets.UTF_16),
            preconditions(fileName = "utf-32.txt", charset = Charsets.UTF_32),
            preconditions(
                fileName = "Romeo-and-Juliet.txt",
                charset = Charset.forName("x-MacCentralEurope"),
            ),
        )

        private fun preconditions(
            fileName: String,
            charset: Charset,
        ) = arrayOf(fileName, charset)
    }
}