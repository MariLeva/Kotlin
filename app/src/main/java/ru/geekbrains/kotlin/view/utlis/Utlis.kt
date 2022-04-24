package ru.geekbrains.kotlin.view.utlis

import ru.geekbrains.kotlin.repository.*

const val KEY_BUNDLE_WEATHER = "weather"
const val KEY_BUNDLE_LON = "lon"
const val KEY_BUNDLE_LAT = "lat"
const val YA_DOMAIN = "https://api.weather.yandex.ru/"
const val YA_ENDPOINT = "v2/informers?"
const val YA_API_KEY = "X-Yandex-API-Key"
const val DETAILS_RESULT = "LOAD RESULT"
const val DETAILS_RESULT_ERROR = "LOAD RESULT ERROR"
const val KEY_WAVE_SERVICE_BROADCAST = "myaction_way"

class Utlis {
}

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather{
    val fact: FactDTO = weatherDTO.fact
    return (Weather(getDefaultCity(), fact.temp, fact.feels_like, fact.condition))
}