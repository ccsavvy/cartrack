package com.jetpack.compose.cartrack.application

import android.app.Application
import android.content.Context

class Cartrack : Application() {

    companion object {
        @Volatile
        lateinit var context: Context
        private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}