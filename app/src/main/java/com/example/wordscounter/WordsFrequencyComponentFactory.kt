package com.example.wordscounter

import android.content.Intent
import androidx.core.app.AppComponentFactory
import com.example.wordscounter.di.scope.RootScope
import com.example.wordscounter.ui.WordsFrequencyActivity

class WordsFrequencyComponentFactory : AppComponentFactory() {
    override fun instantiateActivityCompat(
        cl: ClassLoader,
        className: String,
        intent: Intent?
    ): WordsFrequencyActivity = with(RootScope.scopes.activity) {
        create()
        get().module.wordsFrequencyActivity
    }
}