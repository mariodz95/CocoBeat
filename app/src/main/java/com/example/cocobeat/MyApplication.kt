package com.example.cocobeat

import android.app.Application
import com.ablelib.manager.AbleManager
import com.example.cocobeat.di.ableModule
import com.example.cocobeat.di.databaseModule
import com.example.cocobeat.di.repositoryModule
import com.example.cocobeat.di.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(repositoryModule, viewModule, databaseModule))
        }
        AbleManager.shared.initialize(this)
    }
}