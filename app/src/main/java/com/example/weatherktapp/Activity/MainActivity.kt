package com.example.weatherktapp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherktapp.ViewModel.WeatherViewModel
import com.example.weatherktapp.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val calendar by lazy { Calendar.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        // Set default location information
        val lat = 51.51
        val lon = -0.12
        val locationName = "London"

        binding.apply {
            mainWeatherLocation.text = locationName
            progressLoader.visibility = View.VISIBLE

            weatherViewModel.loadCurrentWeather(lat, lon, "metric") { result ->
                progressLoader.visibility = View.GONE

                result.fold(
                    success = { data ->
                        // Show weather information on successful response
                        weatherInformation.visibility = View.VISIBLE
                        mainWeatherTemp.text = "Now: ${Math.round(data.main.temp)}\u00B0"
                        maxWeather.text = "Max: ${Math.round(data.main.tempMax)}\u00B0"
                        minWeather.text = "Min: ${Math.round(data.main.tempMin)}\u00B0"

                        // Update the background based on the weather and time of day
                        val drawable = if (isNightNow()) {
                            R.drawable.night_bg
                        } else {
                            setDynamicWallpaper(data.weather?.get(0)?.icon ?: "-")
                        }
                        mainBck.setBackgroundResource(drawable)
                    },
                    failure = { error ->
                        // Handle error
                        Toast.makeText(this@MainActivity, "An error occurred: ${error.message}", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }

    private fun isNightNow(): Boolean {
        return calendar.get(Calendar.HOUR_OF_DAY) >= 18
    }

    private fun setDynamicWallpaper(icon: String): Int {
        return when (icon.dropLast(1)) {
            "01" -> R.drawable.snow_bg
            "02", "03", "04" -> R.drawable.cloudy_bg
            "09", "10", "11" -> R.drawable.rainy_bg
            "13" -> R.drawable.snow_bg
            "50" -> R.drawable.haze_bg
            else -> R.drawable.sunny_bg // Add a default background in case no match
        }
    }
}
