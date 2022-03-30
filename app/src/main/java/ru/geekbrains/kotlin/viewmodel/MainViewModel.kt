package ru.geekbrains.kotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class MainViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData()): ViewModel() {

    fun getData() : LiveData<AppState> = liveData

    fun getWeather(){
        Thread {
            liveData.postValue(AppState.Loading)
            sleep(1000)
            if ((0..10).random() > 5)
                liveData.postValue(AppState.Success(Any()))
            else
                liveData.postValue(AppState.Error(IllegalAccessError()))
        }.start()
    }

}