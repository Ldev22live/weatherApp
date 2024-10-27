package com.example.weatherktapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherktapp.Model.ForecastResponseApi
import com.example.weatherktapp.Repository.WeatherRepository
import com.example.weatherktapp.Server.ApiService
import com.github.kittinunf.result.Result

class ForecastViewModel : ViewModel() {

    private val apiKey = "651e65c83907e8834259263057d957f5"  // Replace with your actual API key
    private val apiServices = ApiService(apiKey)
    private val repository = WeatherRepository(apiServices)

    // LiveData for forecast data
    private val _forecastLiveData = MutableLiveData<List<ForecastResponseApi.Item0>?>()
    val forecastLiveData: LiveData<List<ForecastResponseApi.Item0>?> = _forecastLiveData

    // Load forecast data from repository
    fun loadForecast(lat: String, lon: String) {
        repository.getForecastWeather(lat.toDouble(), lon.toDouble(), "metric") { result ->
            when (result) {
                is Result.Success -> {
                    // Filter for next 5 days only
                    val filteredList = filterForecastForNext5Days(result.value.list)
                    _forecastLiveData.postValue(filteredList)
                }
                is Result.Failure -> {
                    // Post a null or empty list if there's an error
                    _forecastLiveData.postValue(null)
                }
            }
        }
    }

    // Utility function to filter forecast data for the next 5 days
    private fun filterForecastForNext5Days(forecastItems: List<ForecastResponseApi.Item0>): List<ForecastResponseApi.Item0> {
        val filteredList = mutableListOf<ForecastResponseApi.Item0>()
        val uniqueDates = mutableSetOf<String>()
        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())

        for (item in forecastItems) {
            val itemDate = dateFormat.parse(item.dtTxt.split(" ")[0])
            if (itemDate != null && (itemDate.after(java.util.Date()) || isSameDay(java.util.Date(), itemDate))) {
                val formattedDate = dateFormat.format(itemDate)
                if (uniqueDates.add(formattedDate)) {
                    filteredList.add(item)
                }
                if (filteredList.size == 5) break
            }
        }
        return filteredList
    }

    private fun isSameDay(date1: java.util.Date, date2: java.util.Date): Boolean {
        val cal1 = java.util.Calendar.getInstance().apply { time = date1 }
        val cal2 = java.util.Calendar.getInstance().apply { time = date2 }
        return cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR) &&
                cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR)
    }
}
