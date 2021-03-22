package com.example.cocobeat.network

data class TemperatureProperty (
    val id: String,
    val name: String,
    val timezone: String,
    val cod: String,
    val main: Main
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)