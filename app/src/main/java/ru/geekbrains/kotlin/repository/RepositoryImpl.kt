package ru.geekbrains.kotlin.repository

class RepositoryImpl: Repository {
    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWorldWeatherFromLocalStorage(): List<Weather> {
        return getWorldCities()
    }

    override fun getRusWeatherFromLocalStorage(): List<Weather> {
        return getRussianCities()
    }
}