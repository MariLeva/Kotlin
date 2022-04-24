package ru.geekbrains.kotlin.repository

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.geekbrains.kotlin.BuildConfig
import ru.geekbrains.kotlin.view.utlis.YA_DOMAIN
import ru.geekbrains.kotlin.view.utlis.convertDtoToModel
import ru.geekbrains.kotlin.viewmodel.DetailsViewModel
import java.io.IOException

class DetailsRepositoryImpl :DetailsRepository{
    override fun getWeatherDetails(city: City, callbackDVM: DetailsViewModel.Callback) {
        val weatherAPI = Retrofit.Builder().apply {
            baseUrl(YA_DOMAIN)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build().create(WeatherAPI::class.java)

        weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY, city.lat, city.lon).enqueue(object :Callback<WeatherDTO>{
            override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        callbackDVM.onResponse(convertDtoToModel(it))
                        val weather = convertDtoToModel(it)
                        weather.city = city
                        callbackDVM.onResponse(weather)
                    }
                }
            }

            override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                callbackDVM.onFail(t as IOException)
            }
        })
    }
}