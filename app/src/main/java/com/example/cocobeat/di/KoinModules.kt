package com.example.cocobeat.di

import android.app.Application
import androidx.room.Room
import com.ablelib.manager.AbleManager
import com.ablelib.manager.IAbleManager
import com.ablelib.storage.AbleDeviceStorage
import com.ablelib.storage.IAbleDeviceStorage
import com.example.cocobeat.database.dao.ReadingDao
import com.example.cocobeat.database.AppDatabase
import com.example.cocobeat.database.dao.DeviceDao
import com.example.cocobeat.model.DeviceViewModel
import com.example.cocobeat.model.DeviceViewModelFactory
import com.example.cocobeat.model.ReadingViewModel
import com.example.cocobeat.model.ReadingViewModelFactory
import com.example.cocobeat.repository.DeviceRepository
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
        ReadingViewModel(get())
    }

    viewModel {
        DeviceViewModel(get())
    }
}

val factoryModule = module{
    factory {
        ReadingViewModelFactory(get())
        DeviceViewModelFactory(get())
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

    fun provideDeviceDao(database: AppDatabase): DeviceDao{
        return database.deviceDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
    single { provideDeviceDao(get()) }

}

val repositoryModule = module {
    fun provideReadingRepository(readingDao: ReadingDao): ReadingRepository {
        return ReadingRepository(readingDao)
    }

    fun provideDeviceRepository(deviceDao: DeviceDao): DeviceRepository {
        return DeviceRepository(deviceDao)
    }

    single {
        provideReadingRepository(get())
    }
    single {
        provideDeviceRepository(get())
    }
}