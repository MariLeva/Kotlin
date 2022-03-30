package ru.geekbrains.kotlin

import android.util.Log
import java.util.*

data class Note(val title: String, val note: String, val data: Date)

enum class DayEnum {
    MORNING,
    DAY,
    EVENING
}

object DataBase {
    fun getWhen(input: DayEnum): String {
        return when (input) {
            DayEnum.MORNING -> "Утро"
            DayEnum.DAY -> "День"
            DayEnum.EVENING -> "Вечер"
        }
    }

    fun getFor() {
        val listDay = listOf("Утро", "День", "Вечер")
        for (day in listDay) {
            Log.d("myLog","${day}")
        }
    }

}