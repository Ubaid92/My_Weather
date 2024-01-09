package com.ubaid.myweather.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.ubaid.myweather.R
import com.ubaid.myweather.databinding.ActivityMainBinding

//0b60d8c6d1705c6a07131882563cc311
class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val searchView = binding.searchView
        mainViewModel = ViewModelProvider(this@MainActivity)[MainViewModel::class.java]
        searchCity(searchView, "karachi")
        mainViewModel.mutableLiveData.observe(this) {
            val temperature = it.main.temp.toString()
            val condition = it.weather.firstOrNull()?.main ?: "Unknown"
            val sunrise = it.sys.sunrise.toLong()
            val sunset = it.sys.sunset.toLong()
            binding.humidity.text = "${it.main.humidity} %"
            binding.windSpeed.text = "${it.wind.speed.toString()} m/s"
            binding.sunRise.text = "${mainViewModel.time(sunrise)}"
            binding.sunset.text = "${mainViewModel.time(sunset)}"
            binding.sea.text = "${it.main.pressure.toString()} hPa"
            binding.weather.text = condition
            binding.temputre.text = "$temperature °C"
            binding.maxTemp.text = "Max Temp: ${it.main.temp_max.toString()} °C"
            binding.minTemp.text = "Min Temp: ${it.main.temp_min.toString()} °C"
            binding.day.text = mainViewModel.dayName(System.currentTimeMillis())
            binding.date.text = mainViewModel.date()
            changeBackGround(condition)

        }

    }

    fun searchCity(searchView: SearchView, city: String) {
        mainViewModel.fatchWeatherData(city)
        binding.cityName.text = city
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    mainViewModel.fatchWeatherData(query)
                    binding.cityName.text = query
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    fun changeBackGround(conditions: String) {
        when (conditions) {
            "Clear Sky", "Sunny", "Clear" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

            "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy" -> {
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }

            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }

            "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard" -> {
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


}

