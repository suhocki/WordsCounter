package com.example.wordscounter.domain

import java.io.File
import java.nio.charset.Charset
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class CharsetDetectorTest(
    private val fileName: String,
    private val charset: Charset,
) {
    private val detector = CharsetDetector(
        mapOf(
            "utf-8" to Charsets.UTF_8,
            "utf-16" to Charsets.UTF_16,
            "utf-32" to Charsets.UTF_32,
        )
    )

    @Test
    fun `detect charset`() = runTest {
        val file = File(javaClass.classLoader.getResource(fileName).file)

        assertEquals(
            charset,
            detector.detectFileCharset(file, "^[0-9A-z\\s]+\$".toRegex())
        )
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            preconditions(fileName = "utf-8.txt", charset = Charsets.UTF_8),
            preconditions(fileName = "utf-16.txt", charset = Charsets.UTF_16),
            preconditions(fileName = "utf-32.txt", charset = Charsets.UTF_32),
        )

        private fun preconditions(
            fileName: String,
            charset: Charset,
        ) = arrayOf(fileName, charset)
    }
}