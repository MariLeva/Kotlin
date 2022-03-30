package ru.geekbrains.kotlin.viewmodel

sealed class AppState {
    data class Success(val weatherData: Any): AppState(){
        fun test(){}
    }
    data class Error(val error: Throwable): AppState(){}
    object Loading: AppState()
}