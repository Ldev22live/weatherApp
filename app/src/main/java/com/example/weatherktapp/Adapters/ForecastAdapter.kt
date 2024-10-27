package com.example.weatherktapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherktapp.Model.ForecastResponse
import com.example.weatherktapp.databinding.ItemWeatherForecastBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter(private val forecastList: List<ForecastResponse.ForecastItem>) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWeatherForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = forecastList[position]

        if (forecast.weather.isNotEmpty()) {
            val weather = forecast.weather[0] // Get the first weather item
            val forecastWeather = "${toUpper(weather.description)} ${forecast.main.tempMax}\u00B0/${forecast.main.tempMin}\u00B0"
            holder.binding.temperatureTextView.text = forecastWeather
            holder.binding.dateTextView.text = getDayOfWeek(forecast.dtTxt)
            val iconUrl = "https://openweathermap.org/img/wn/${weather.icon}@2x.png"
            Picasso.get().load(iconUrl).into(holder.binding.iconWeather)
        } else {
            // Handle case when forecast.weather is empty
            holder.binding.temperatureTextView.text = ""
            holder.binding.iconWeather.setImageResource(R.drawable.ic_launcher_background)
        }
    }

    override fun getItemCount(): Int = forecastList.size

    class ViewHolder(val binding: ItemWeatherForecastBinding) : RecyclerView.ViewHolder(binding.root)

    private fun getDayOfWeek(weatherDay: String): String {
        val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = try {
            dateFormat.parse(weatherDay.split(" ")[0])
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        } ?: return ""

        val weatherCalendar = Calendar.getInstance().apply { time = date }
        val weatherDayOfWeek = weatherCalendar.get(Calendar.DAY_OF_WEEK)

        return if (weatherDayOfWeek == currentDayOfWeek) {
            "Today"
        } else {
            val dayDifference = (weatherDayOfWeek - currentDayOfWeek + 7) % 7
            val nextDay = (currentDayOfWeek + dayDifference) % 7
            val daysOfWeek = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            daysOfWeek[nextDay]
        }
    }

    private fun toUpper(input: String): String {
        return input.replaceFirstChar { it.uppercase() }
    }
}
