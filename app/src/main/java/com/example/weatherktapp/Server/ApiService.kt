package com.example.weatherktapp.Server

import com.example.weatherktapp.Model.CurrentResponseApi
import com.example.weatherktapp.Model.ForecastResponseApi
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.gson.responseObject
import com.google.gson.Gson

class ApiService(private val apiKey: String) {

    private val baseUrl = "https://api.openweathermap.org/data/2.5/"

    fun getCurrentWeather(
        lat: Double,
        lon: Double,
        units: String,
        callback: (com.github.kittinunf.result.Result<CurrentResponseApi, FuelError>) -> Unit
    ) {
        val parameters = listOf(
            "lat" to lat,
            "lon" to lon,
            "units" to units,
            "appid" to apiKey
        )

        Fuel.get("${baseUrl}weather", parameters)
            .responseObject<CurrentResponseApi>(Gson()) { _, _, result ->
                callback(result)
            }
    }

    fun getForecastWeather(lat: Double,
                           lon: Double,
                           units: String,
                           callback: (com.github.kittinunf.result.Result<ForecastResponseApi, FuelError>) -> Unit
    ) {
        val parameters = listOf(
            "lat" to lat,
            "lon" to lon,
            "units" to units,
            "appid" to apiKey
        )

        Fuel.get("${baseUrl}forecast", parameters)
            .responseObject<ForecastResponseApi>(Gson()) { _, _, result ->
                callback(result)
            }
    }
}
