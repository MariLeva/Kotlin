package ru.geekbrains.kotlin.repository

class RepositoryImpl: Repository {
    override fun getWeatherFromServer() = Weather()

    override fun getWorldWeatherFromLocalStorage() = getWorldCities()

    override fun getRusWeatherFromLocalStorage() = getRussianCities()
}