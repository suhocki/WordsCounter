package com.example.wordscounter.data

import android.content.res.AssetManager
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileInputStream

class AssetsRepositoryTest {
    private val manager: AssetManager = mockk()

    private val fileName = "utf-16.txt"

    private val cacheDir = javaClass.classLoader.let(::requireNotNull)
        .getResource(fileName).file.let(::File)
        .parentFile.let(::requireNotNull)
        .let { parent -> File(parent, "cache").apply { mkdir() } }

    private val inputStream: FileInputStream
        get() = requireNotNull(javaClass.classLoader)
            .getResource(fileName).file.let(::File)
            .inputStream()

    private val repository = AssetsRepository(manager, cacheDir)

    @Before
    fun setUp() {
        every { manager.open(fileName) } answers { inputStream }
    }

    @Test
    fun `get file`() = runTest {
        assertEquals(
            inputStream.bufferedReader(Charsets.UTF_16).readLines(),
            repository.getFile(fileName).readLines(Charsets.UTF_16),
        )
    }
}