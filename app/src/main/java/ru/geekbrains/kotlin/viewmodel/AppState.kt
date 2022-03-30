package ru.geekbrains.kotlin.viewmodel

import ru.geekbrains.kotlin.repository.Weather

sealed class AppState {
    data class Success(val weatherData: Weather): AppState(){
        fun test(){}
    }
    data class Error(val error: Throwable): AppState(){}
    object Loading: AppState()
}