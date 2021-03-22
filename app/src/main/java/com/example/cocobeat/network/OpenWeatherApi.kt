package com.example.cocobeat.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url


interface OpenWeatherApi {
        @GET
        suspend fun getTemperature(@Url url: String): Response<TemperatureProperty>
}
