package ru.geekbrains.kotlin.view.utlis

import ru.geekbrains.kotlin.repository.*
import ru.geekbrains.kotlin.room.HistoryEntity

const val KEY_BUNDLE_WEATHER = "weather"
const val KEY_BUNDLE_LON = "lon"
const val KEY_BUNDLE_LAT = "lat"
const val YA_DOMAIN = "https://api.weather.yandex.ru/"
const val YA_ENDPOINT = "v2/informers?"
const val YA_API_KEY = "X-Yandex-API-Key"
const val DETAILS_RESULT = "LOAD RESULT"
const val DETAILS_RESULT_ERROR = "LOAD RESULT ERROR"
const val KEY_WAVE_SERVICE_BROADCAST = "myaction_way"
const val IS_WORLD_KEY = "LIST_OF_TOWNS"
const val REQUEST_CODE = 999
const val REQUEST_CODE_LOCATION = 998

class Utlis {
}

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather{
    val fact: FactDTO = weatherDTO.fact
    return (Weather(getDefaultCity(), fact.temp, fact.feels_like, fact.condition, fact.icon))
}

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(City(it.city, 0.0, 0.0), it.temperature, it.feelsLike, it.condition, it.icon)
    }
}

fun convertWeatherToEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(0, weather.city.name, weather.temperature, weather.feelsLike, weather.condition, weather.icon)
}