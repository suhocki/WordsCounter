package com.example.wordscounter.di

interface Scope {
    val module: Module

    fun clear() {
        module.clear()
    }
}