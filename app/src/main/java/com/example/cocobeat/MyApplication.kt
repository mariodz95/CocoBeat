package com.example.cocobeat

import android.app.Application
import com.ablelib.manager.AbleManager
import com.ablelib.util.AbleLogOptions
import com.example.cocobeat.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(repositoryModule, viewModule, databaseModule, ableModule, factoryModule))
        }
        AbleManager.shared.initialize(this)
        AbleManager.shared.loggingOptions = AbleLogOptions.Full
    }
}