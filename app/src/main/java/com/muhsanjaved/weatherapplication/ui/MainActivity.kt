package com.muhsanjaved.weatherapplication.ui

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.muhsanjaved.weatherapplication.R
import com.muhsanjaved.weatherapplication.databinding.ActivityMainBinding
import com.muhsanjaved.weatherapplication.models.WeatherApp
import com.muhsanjaved.weatherapplication.network.ApiController
import com.muhsanjaved.weatherapplication.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import java.util.Date
import java.util.Locale

// API KEY :::    4e2d2d137d34778b5b616949c2704a9b
// https://api.openweathermap.org/data/2.5/weather?q=qambar&appid=4e2d2d137d34778b5b616949c2704a9b
class MainActivity : AppCompatActivity() {
    private val MyTAG: String = "MyTAG"

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fetchWeatherData("qambar")
        searchCity()
    }

    private fun searchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(cityName: String) {

        var response = ApiController()
            .getApiInterface()
            ?.getWeatherData(cityName, "4e2d2d137d34778b5b616949c2704a9b", "metric")

        response?.enqueue(object : Callback<WeatherApp> {
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
//                val responseBody = response.body()
                if (response.isSuccessful) {
                    setDataOnView(response.body(), cityName)
                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                Toast.makeText(this@MainActivity, "ERROR", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun setDataOnView(body: WeatherApp?, cityName: String) {
        if (body != null) {
            val temperature = body.main.temp.toString()
            val humidity = body.main.humidity
            val windSpeed = body.wind.speed
            val sunRise = body.sys.sunrise.toLong()
            val sunSet = body.sys.sunset.toLong()
            val seaLevel = body.main.pressure
            val condition = body.weather.firstOrNull()?.main ?: "unknown"
            val maxTemp = body.main.temp_max
            val minTemp = body.main.temp_min

            // setup app UI
            binding.temp.text = "$temperature °C"
            binding.weather.text = condition
            binding.maxTemp.text = "Max Temp : $maxTemp °C"
            binding.minTemp.text = "Min Temp : $minTemp °C"
            binding.humidity.text = "$humidity %"
            binding.windSpeed.text = "$windSpeed m/s"
            binding.sunrise.text = "${time(sunRise)}"
            binding.sunset.text = "${time(sunSet)}"
            binding.sea.text = "$seaLevel hPa"
            binding.conditions.text = condition
            binding.day.text = dayName(System.currentTimeMillis())
            binding.date.text = date()
            binding.cityName.text = cityName
            //Log.d(MyTAG, "onResponse: $temperature" )

            changeImagesAccordingToWeatherCondition(condition)
        }
    }

    private fun changeImagesAccordingToWeatherCondition(condition: String) {
        when (condition) {
            "Clear Sky", "Sunny", "Clear" -> {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.statusBarColor = resources.getColor(R.color.clear)
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

            "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy" -> {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.statusBarColor = resources.getColor(R.color.clouds)
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }

            "Drizzle", "Moderate Rain", "Showers", "Heavy Rain" -> {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.statusBarColor = resources.getColor(R.color.rain)
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }

            "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard" -> {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.statusBarColor = resources.getColor(R.color.snow)
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

            else -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun time(timestemp: Long): String? {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestemp * 1000)))
    }

    private fun date(): String? {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }

    fun dayName(timestemp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
}