package ru.geekbrains.kotlin.viewmodel

import ru.geekbrains.kotlin.repository.Weather

sealed class AppState {
    data class Success(val weatherData: List<Weather>): AppState(){}

    data class Error(val error: Throwable): AppState(){}

    object Loading: AppState()
}