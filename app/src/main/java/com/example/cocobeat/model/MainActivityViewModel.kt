package com.example.cocobeat.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocobeat.network.TemperatureProperty
import com.example.cocobeat.repository.OpenWeatherRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class MainActivityViewModel(private val repository: OpenWeatherRepository) : ViewModel()  {
    val temperature: MutableLiveData<Response<TemperatureProperty>> = MutableLiveData()

    fun getTemperature(url: String){
        viewModelScope.launch {
            try {
                val response = repository.getTemperature(url)
                temperature.value = response
            }catch (e: Exception)
            {
                Log.v("test", "errorčin $e")
            }

        }
    }
}