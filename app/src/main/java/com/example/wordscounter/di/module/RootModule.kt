package com.example.wordscounter.di.module

import android.app.Application
import android.content.Context
import com.example.wordscounter.di.Module

class RootModule(
    private val application: Application
) : Module {
    val context: Context
        get() = application.applicationContext
}