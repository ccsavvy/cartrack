package com.jetpack.compose.cartrack.application

import android.app.Application
import android.content.Context

class Cartrack : Application() {

    companion object {
        @Volatile
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}