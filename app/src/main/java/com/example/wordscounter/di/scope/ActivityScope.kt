package com.example.wordscounter.di.scope

import com.example.wordscounter.di.Scope
import com.example.wordscounter.di.module.ActivityModule

class ActivityScope(
) : Scope {
    override val module = ActivityModule()
}