package com.example.weatherktapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherktapp.databinding.ActivityForecastBinding

class ForecastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForecastBinding
    private val forecastViewModel: ForecastViewModel by viewModels() // Initialize ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView with adapter
        binding.dayForecast.apply {
            layoutManager = LinearLayoutManager(this@ForecastActivity)
        }

        // Retrieve latitude and longitude, default to 0 if not provided
        val latitude = intent.getStringExtra("latitude") ?: "0.0"
        val longitude = intent.getStringExtra("longitude") ?: "0.0"

        // Observe forecast data from ViewModel
        forecastViewModel.forecastLiveData.observe(this) { forecastItems ->
            forecastItems?.let {
                val adapter = ForecastAdapter(it)
                binding.dayForecast.adapter = adapter
            }
        }

        // Load forecast data using ViewModel
        forecastViewModel.loadForecast(latitude, longitude)
    }
}
