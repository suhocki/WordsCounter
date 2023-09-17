package com.example.wordscounter

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import com.example.wordscounter.di.scope.RootScope

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        RootScope.init(this)
    }

    override fun startInstrumentation(
        className: ComponentName,
        profileFile: String?,
        arguments: Bundle?
    ): Boolean {
        return super.startInstrumentation(className, profileFile, arguments)
    }
    override fun startActivity(intent: Intent?) {
        RootScope.scopes.activity.create()
        super.startActivity(intent)
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        super.startActivity(intent, options)
    }
}