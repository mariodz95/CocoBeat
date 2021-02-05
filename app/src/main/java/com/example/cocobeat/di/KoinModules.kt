package com.example.cocobeat.di

import android.app.Application
import androidx.room.Room
import com.ablelib.manager.AbleManager
import com.ablelib.manager.IAbleManager
import com.ablelib.storage.AbleDeviceStorage
import com.ablelib.storage.IAbleDeviceStorage
import com.example.cocobeat.database.dao.ReadingDao
import com.example.cocobeat.database.AppDatabase
import com.example.cocobeat.model.MainActivityViewModel
import com.example.cocobeat.repository.ReadingRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ableModule = module {
    single<IAbleManager> { AbleManager.shared }
    single<IAbleDeviceStorage> { AbleDeviceStorage.default }
}


val viewModule = module{
    viewModel{
        MainActivityViewModel(get())
    }
}

val databaseModule = module {

    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "eds.database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    fun provideDao(database: AppDatabase): ReadingDao {
        return database.readingDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
}


val repositoryModule = module {
    fun provideReadingRepository(readingDao: ReadingDao): ReadingRepository {
        return ReadingRepository(readingDao)
    }
    single {
        provideReadingRepository(get())
    }
}