package com.ubaid.myweather.activities

import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ubaid.myweather.ApiInterface
import com.ubaid.myweather.data.WeatherData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel:ViewModel() {
    val mutableLiveData = MutableLiveData<WeatherData>()
    var mainActivity = MainActivity()



    fun fatchWeatherData(cityName:String,): MutableLiveData<WeatherData> {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)


        val response = retrofit.getWeatherData(cityName, "0b60d8c6d1705c6a07131882563cc311", "metric")
        response.enqueue(object : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    mutableLiveData.postValue(responseBody)


                }

            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
         return mutableLiveData
    }



    fun dayName(timeStamp:Long):String{
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }

    fun date():String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }

    fun time(timeStamp: Long):String{
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timeStamp* 1000)))
    }

}