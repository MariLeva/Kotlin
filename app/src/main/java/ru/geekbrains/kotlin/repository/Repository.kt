package ru.geekbrains.kotlin.repository

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWorldWeatherFromLocalStorage(): List<Weather>
    fun getRusWeatherFromLocalStorage(): List<Weather>
}