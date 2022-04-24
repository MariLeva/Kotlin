package ru.geekbrains.kotlin.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.geekbrains.kotlin.view.utlis.YA_API_KEY
import ru.geekbrains.kotlin.view.utlis.YA_ENDPOINT

interface WeatherAPI {
    @GET(YA_ENDPOINT)
    fun getWeather(
        @Header(YA_API_KEY) apikey: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<WeatherDTO>
}