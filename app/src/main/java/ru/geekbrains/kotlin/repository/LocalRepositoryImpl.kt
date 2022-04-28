package ru.geekbrains.kotlin.repository

import ru.geekbrains.kotlin.room.HistoryDao
import ru.geekbrains.kotlin.view.utlis.convertHistoryEntityToWeather
import ru.geekbrains.kotlin.view.utlis.convertWeatherToEntity

class LocalRepositoryImpl (private val localDataSource: HistoryDao) :LocalRepository{
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToEntity(weather))
    }
}