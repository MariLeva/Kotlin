package ru.geekbrains.kotlin.repository

interface LocalRepository {
    fun getAllHistory(): List<Weather>
    fun saveEntity (weather: Weather)
}