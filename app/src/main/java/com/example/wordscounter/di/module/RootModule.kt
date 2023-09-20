package com.example.wordscounter.di.module

import android.app.Application
import android.content.Context
import com.example.wordscounter.di.Module
import com.example.wordscounter.domain.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RootModule(
    private val application: Application
) : Module {
    val context: Context
        get() = application.applicationContext

    val coroutineDispatchers: CoroutineDispatchers
        get() = object : CoroutineDispatchers {
            override val main: CoroutineDispatcher
                get() = Dispatchers.Main
            override val io: CoroutineDispatcher
                get() = Dispatchers.IO
            override val default: CoroutineDispatcher
                get() = Dispatchers.Default
            override val unconfined: CoroutineDispatcher
                get() = Dispatchers.Unconfined
        }
}