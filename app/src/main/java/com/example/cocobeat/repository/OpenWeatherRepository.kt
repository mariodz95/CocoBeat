package com.example.cocobeat.repository

import com.example.cocobeat.network.RetrofitInstance
import com.example.cocobeat.network.TemperatureProperty
import retrofit2.Response

class OpenWeatherRepository{
    suspend fun getTemperature(url: String) : Response<TemperatureProperty> {
        return RetrofitInstance.api.getTemperature(url)
    }
}



