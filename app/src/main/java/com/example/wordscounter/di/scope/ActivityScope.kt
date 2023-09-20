package com.example.wordscounter.di.scope

import android.content.Context
import com.example.wordscounter.di.Scope
import com.example.wordscounter.di.module.ActivityModule
import com.example.wordscounter.domain.CoroutineDispatchers

class ActivityScope(
    context: Context,
    coroutineDispatchers: CoroutineDispatchers,
) : Scope {
    override val module = ActivityModule(context, coroutineDispatchers)
}