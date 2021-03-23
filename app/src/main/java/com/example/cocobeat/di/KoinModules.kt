package com.example.cocobeat.di

import android.app.Application
import androidx.room.Room
import com.ablelib.manager.AbleManager
import com.ablelib.manager.IAbleManager
import com.ablelib.storage.AbleDeviceStorage
import com.ablelib.storage.IAbleDeviceStorage
import com.example.cocobeat.database.AppDatabase
import com.example.cocobeat.database.dao.*
import com.example.cocobeat.model.*
import com.example.cocobeat.repository.*
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

    viewModel {
        StepViewModel(get())
    }

    viewModel {
        MainActivityViewModel(get())
    }

    viewModel {
        FoodViewModel(get())
    }
}

val factoryModule = module{
    factory {
        ReadingViewModelFactory(get())
        DeviceViewModelFactory(get())
        ExerciseViewModelFactory(get())
        StepViewModelFactory(get())
        MainActivityViewModelFactory(get())
        FoodViewModelFactory(get())
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

    fun provideStepDao(database: AppDatabase): StepDao {
        return database.stepDao()
    }

    fun provideFoodDao(database: AppDatabase): FoodDao {
        return database.foodDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
    single { provideDeviceDao(get()) }
    single { provideExerciseDao(get()) }
    single { provideStepDao(get()) }
    single { provideFoodDao(get()) }
}

val repositoryModule = module {
    fun provideReadingRepository(readingDao: ReadingDao): ReadingRepository {
        return ReadingRepository(readingDao)
    }

    fun provideDeviceRepository(deviceDao: DeviceDao): DeviceRepository {
        return DeviceRepository(deviceDao)
    }

    fun provideExerciseRepository(exerciseDao: ExerciseDao): ExerciseRepository {
        return ExerciseRepository(exerciseDao)
    }

    fun provideSteRepository(stepDao: StepDao): StepRepository {
        return StepRepository(stepDao)
    }

    fun provideOpenWeatherRepository(): OpenWeatherRepository {
        return OpenWeatherRepository()
    }

    fun provideFoodRepository(foodDao: FoodDao): FoodRepository {
        return FoodRepository(foodDao)
    }

    single { provideReadingRepository(get()) }
    single { provideDeviceRepository(get()) }
    single { provideExerciseRepository(get()) }
    single { provideSteRepository(get()) }
    single { provideOpenWeatherRepository() }
    single { provideFoodRepository(get()) }
}