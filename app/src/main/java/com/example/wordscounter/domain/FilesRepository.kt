package com.example.wordscounter.domain

import java.io.File

interface FilesRepository {
    suspend fun getFile(fileName: String): File
}