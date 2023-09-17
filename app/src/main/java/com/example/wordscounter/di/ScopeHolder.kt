package com.example.wordscounter.di

import androidx.annotation.CallSuper

abstract class ScopeHolder <T: Scope> {
    protected var scope: T? = null

    abstract fun create()

    fun get() = requireNotNull(scope)

    fun isOpen() = scope != null

    @CallSuper
    open fun clear() {
        scope?.clear()
        scope = null
    }
}