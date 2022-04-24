package ru.geekbrains.kotlin.repository

import ru.geekbrains.kotlin.viewmodel.DetailsViewModel
import javax.security.auth.callback.Callback

interface DetailsRepository {
    fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback)
}