package com.example.wordscounter.di.scope

import android.app.Application
import android.content.Context
import com.example.wordscounter.di.Scope
import com.example.wordscounter.di.ScopeHolder
import com.example.wordscounter.di.module.RootModule

object RootScope : Scope {
    private var application: Application? = null

    override val module: RootModule by lazy {
        RootModule(requireNotNull(application))
    }

    val scopes by lazy {
        with(module) { Scopes(context) }
    }

    fun init(application: Application) {
        this.application = application
    }

    class Scopes(
        private val context: Context,
    ) {
        val activity = ActivityScopeHolder()

        inner class ActivityScopeHolder : ScopeHolder<ActivityScope>() {
            override fun create() {
                scope = ActivityScope(context)
            }
        }
    }
}