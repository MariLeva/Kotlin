package ru.geekbrains.kotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.kotlin.repository.City
import ru.geekbrains.kotlin.repository.DetailsRepository
import ru.geekbrains.kotlin.repository.DetailsRepositoryImpl
import ru.geekbrains.kotlin.repository.Weather
import java.io.IOException
import java.lang.Exception

class DetailsViewModel (
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repository: DetailsRepository = DetailsRepositoryImpl()
): ViewModel(){

    fun getLiveData() = liveData

    fun getWeather(city: City){
        liveData.postValue(DetailsState.Loading)
        repository.getWeatherDetails(city, object  :Callback{
            override fun onResponse(weather: Weather){
                liveData.postValue(DetailsState.Success(weather))
            }

            override fun onFail(e: IOException) {
                liveData.postValue(DetailsState.Error(Throwable(e.message)))
            }
        })
    }

    interface Callback{
        fun onResponse(weather: Weather)
        fun onFail(exception: IOException)
    }
}