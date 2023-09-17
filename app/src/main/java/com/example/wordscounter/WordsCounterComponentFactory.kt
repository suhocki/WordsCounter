package com.example.wordscounter

import android.content.Intent
import androidx.core.app.AppComponentFactory
import com.example.wordscounter.di.scope.RootScope

class WordsCounterComponentFactory : AppComponentFactory() {
    override fun instantiateActivityCompat(
        cl: ClassLoader,
        className: String,
        intent: Intent?
    ): WordsCounterActivity = with(RootScope.scopes.activity) {
        create()
        get().module.wordsCounterActivity
    }
}