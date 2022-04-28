package ru.geekbrains.kotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.kotlin.App.Companion.getHistoryDao
import ru.geekbrains.kotlin.repository.*
import java.io.IOException
import java.lang.Exception

class DetailsViewModel (
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repository: DetailsRepository = DetailsRepositoryImpl(),
    private val historyRepository: LocalRepository = LocalRepositoryImpl(getHistoryDao())
): ViewModel(){

    fun getLiveData() = liveData

    fun getWeather(city: City){
        repository.getWeatherDetails(city, object  :Callback{
            override fun onResponse(weather: Weather){
                liveData.postValue(DetailsState.Success(weather))
            }

            override fun onFail(e: IOException) {
                liveData.postValue(DetailsState.Error(Throwable(e.message)))
            }
        })
    }

    fun saveCityToDB(weather: Weather){
        historyRepository.saveEntity(weather)
    }

    interface Callback{
        fun onResponse(weather: Weather)
        fun onFail(exception: IOException)
    }
}