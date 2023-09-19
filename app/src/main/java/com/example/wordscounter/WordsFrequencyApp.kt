package com.example.wordscounter

import android.app.Application
import com.example.wordscounter.di.scope.RootScope

class WordsFrequencyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RootScope.init(this)
    }
}