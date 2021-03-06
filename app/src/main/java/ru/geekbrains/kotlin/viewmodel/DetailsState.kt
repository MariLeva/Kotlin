package ru.geekbrains.kotlin.viewmodel

import ru.geekbrains.kotlin.repository.Weather

sealed class DetailsState{
    data class Success(val weather: Weather): DetailsState()
    data class Error(val error: Throwable): DetailsState()
}
