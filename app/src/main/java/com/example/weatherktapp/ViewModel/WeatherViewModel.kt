package com.example.weatherktapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.weatherktapp.Model.CurrentResponseApi
import com.example.weatherktapp.Repository.WeatherRepository
import com.example.weatherktapp.Server.ApiService
import com.github.kittinunf.result.Result
import com.github.kittinunf.fuel.core.FuelError

class WeatherViewModel : ViewModel() {

    private val apiKey = "651e65c83907e8834259263057d957f5"  // Replace with your actual API key
    private val apiServices = ApiService(apiKey)
    private val repository = WeatherRepository(apiServices)

    fun loadCurrentWeather(
        lat: Double,
        lng: Double,
        units: String,
        callback: (Result<CurrentResponseApi, FuelError>) -> Unit
    ) {
        repository.getCurrentWeather(lat, lng, units) { result ->
            callback(result)
        }
    }
}
