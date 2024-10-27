package com.example.weatherktapp.Repository

import com.example.weatherktapp.Model.CurrentResponseApi
import com.example.weatherktapp.Model.ForecastResponseApi
import com.example.weatherktapp.Server.ApiService
import com.github.kittinunf.result.Result
import com.github.kittinunf.fuel.core.FuelError

class WeatherRepository(private val apiServices: ApiService) {

    fun getCurrentWeather(
        lat: Double,
        lng: Double,
        units: String,
        callback: (Result<CurrentResponseApi, FuelError>) -> Unit
    ) {
        apiServices.getCurrentWeather(lat, lng, units) { result ->
            callback(result)
        }
    }

    fun getForecastWeather(
        lat: Double,
        lng: Double,
        units: String,
        callback: (Result<ForecastResponseApi, FuelError>) -> Unit
    ) {
        apiServices.getForecastWeather(lat, lng, units) { result ->
            callback(result)
        }
    }
}
