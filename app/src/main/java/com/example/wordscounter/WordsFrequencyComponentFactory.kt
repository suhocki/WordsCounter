package com.example.wordscounter

import android.app.Activity
import android.content.Intent
import androidx.core.app.AppComponentFactory
import com.example.wordscounter.di.scope.RootScope

class WordsFrequencyComponentFactory : AppComponentFactory() {
    override fun instantiateActivityCompat(
        cl: ClassLoader,
        className: String,
        intent: Intent?
    ): Activity = with(RootScope.scopes.activity) {
        if (!isOpen()) {
            create()
        }
        get().module.wordsFrequencyActivity
    }
}