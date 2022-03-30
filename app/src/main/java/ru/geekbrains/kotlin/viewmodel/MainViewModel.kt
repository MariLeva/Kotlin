package ru.geekbrains.kotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class MainViewModel(private val liveData: MutableLiveData<Any> = MutableLiveData()): ViewModel() {

    fun getData() : LiveData<Any> = liveData

    fun getWeather(){
        Thread {
            sleep(1000)
            liveData.postValue(Any())
        }.start()
    }

}