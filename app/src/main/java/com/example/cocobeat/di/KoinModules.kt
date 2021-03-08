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
import com.example.cocobeat.database.dao.ExerciseDao
import com.example.cocobeat.model.*
import com.example.cocobeat.repository.DeviceRepository
import com.example.cocobeat.repository.ExerciseRepository
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

    viewModel {
        ExerciseViewModel(get())
    }
}

val factoryModule = module{
    factory {
        ReadingViewModelFactory(get())
        DeviceViewModelFactory(get())
        ExerciseViewModelFactory(get())
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

    fun provideExerciseDao(database: AppDatabase): ExerciseDao {
        return database.exerciseDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
    single { provideDeviceDao(get()) }
    single { provideExerciseDao(get()) }
}

val repositoryModule = module {
    fun provideReadingRepository(readingDao: ReadingDao): ReadingRepository {
        return ReadingRepository(readingDao)
    }

    fun provideDeviceRepository(deviceDao: DeviceDao): DeviceRepository {
        return DeviceRepository(deviceDao)
    }

    fun provideExerciseDao(exerciseDao: ExerciseDao): ExerciseRepository {
        return ExerciseRepository(exerciseDao)
    }

    single {
        provideReadingRepository(get())
    }
    single {
        provideDeviceRepository(get())
    }
    single {
        provideExerciseDao(get())
    }
}