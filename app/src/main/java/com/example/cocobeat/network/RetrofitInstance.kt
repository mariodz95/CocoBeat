package com.example.cocobeat.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.openweathermap.org"

    private val retrofit by lazy {
         Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
    }

    val api: OpenWeatherApi by lazy{
        retrofit.create(OpenWeatherApi::class.java)
    }
}