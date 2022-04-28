package ru.geekbrains.kotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.kotlin.App.Companion.getHistoryDao
import ru.geekbrains.kotlin.repository.LocalRepositoryImpl

class HistoryViewModel(
    private val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: LocalRepositoryImpl = LocalRepositoryImpl(getHistoryDao())
) : ViewModel() {

    fun getAllHistory() {
        historyLiveData.value = AppState.Loading
        historyLiveData.value = AppState.Success(repositoryImpl.getAllHistory())
    }

    fun getData(): LiveData<AppState> {
        return historyLiveData
    }
}