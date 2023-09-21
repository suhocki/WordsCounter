package com.example.wordscounter.data

import android.content.res.AssetManager
import com.example.wordscounter.domain.FilesRepository
import java.io.File

class AssetsRepository(
    private val assetManager: AssetManager,
    private val cacheDir: File,
) : FilesRepository {
    override suspend fun getFile(
        fileName: String
    ): File = File(cacheDir, fileName).apply {
        writeBytes(assetManager.open(fileName).readBytes())
    }
}