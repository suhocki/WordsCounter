package com.example.wordscounter.di.scope

import android.content.Context
import com.example.wordscounter.di.Scope
import com.example.wordscounter.di.module.ActivityModule

class ActivityScope(
    context: Context,
) : Scope {
    override val module = ActivityModule(context)
}