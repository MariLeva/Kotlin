package ru.geekbrains.kotlin.repository

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalServer(): Weather
}