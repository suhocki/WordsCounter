package com.example.wordscounter

import android.content.Intent
import androidx.core.app.AppComponentFactory
import com.example.wordscounter.di.scope.RootScope

class WordsComponentFactory : AppComponentFactory() {
    override fun instantiateActivityCompat(
        cl: ClassLoader,
        className: String,
        intent: Intent?
    ): WordsActivity = with(RootScope.scopes.activity) {
        create()
        get().module.wordsActivity
    }
}