package ru.geekbrains.kotlin.room

import androidx.room.*

@Dao
interface HistoryDao {

    @Query("SELECT * FROM HistoryEntity")
    fun all(): List<HistoryEntity>

    @Query("INSERT INTO HistoryEntity (city, temperature, feelsLike, condition, icon) VALUES (:city, :temperature, :feelsLike, :condition, :icon)")
    fun nativeInsert(city: String, temperature: Int, feelsLike: Int, condition: String, icon: String)

    @Query("SELECT * FROM HistoryEntity WHERE city=:city")
    fun getHistoryForCity(city: String): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryEntity)

    @Update
    fun update(entity: HistoryEntity)

    @Delete
    fun delete(entity: HistoryEntity)
}