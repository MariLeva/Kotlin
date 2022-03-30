package ru.geekbrains.kotlin.repository

class RepositoryImpl: Repository {
    override fun getWeatherFromServer(): Weather {
        Thread.sleep(2000L)
        return Weather()
    }

    override fun getWeatherFromLocalServer(): Weather {
        Thread.sleep(20L)
        return Weather()
    }
}