package ru.geekbrains.kotlin.view.weatherList

import ru.geekbrains.kotlin.repository.Weather

interface OnItemClickListener {
    fun onItemClick(weather: Weather)
}