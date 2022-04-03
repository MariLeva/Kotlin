package ru.geekbrains.kotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.kotlin.repository.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData(),
                    private val repository: RepositoryImpl = RepositoryImpl()):
                    ViewModel() {

    fun getData() : LiveData<AppState> = liveData

    fun getWeatherFromLocalRus() = getWeatherFromLocal(true)

    fun getWeatherFromLocalWorld() = getWeatherFromLocal(false)

    fun getWeatherFromServer() = getWeatherFromRemote()

    fun getWeatherFromLocal(isRus: Boolean){
        liveData.value = AppState.Loading
        Thread {
            sleep(1000)
            liveData.postValue(AppState.Success(
                    if (isRus) repository.getRusWeatherFromLocalStorage()
                    else repository.getWorldWeatherFromLocalStorage()))
        }.start()
    }

    fun getWeatherFromRemote(){
        liveData.value = AppState.Loading
        Thread{
            sleep(1000)
            liveData.postValue(AppState.Error(Throwable()))
        }.start()
    }

}