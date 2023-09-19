package com.example.wordscounter.data

import android.content.Context
import android.content.res.AssetManager
import com.example.wordscounter.domain.FilesRepository
import java.io.File

class AssetsRepository(
    private val assetManager: AssetManager,
    private val context: Context,
) : FilesRepository {
    override suspend fun getFile(
        fileName: String
    ): File = File(context.filesDir, fileName).apply {
        writeBytes(assetManager.open(fileName).readBytes())
    }
}