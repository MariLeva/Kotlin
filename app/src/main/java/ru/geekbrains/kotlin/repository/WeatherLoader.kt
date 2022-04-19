package ru.geekbrains.kotlin.repository

interface WeatherLoader {
    fun onLoaded(weatherDTO: WeatherDTO)
    fun onFailed(throwable: Throwable)
}