package ru.geekbrains.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val noteOne = Note("Утро", "Прогулка с собакой", Date())
        val noteTwo = noteOne.copy()

        findViewById<Button>(R.id.button).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                findViewById<TextView>(R.id.textViewOne).text = noteOne.title + " " + noteOne.note + " " + noteOne.data
                findViewById<TextView>(R.id.textViewTwo).text = noteTwo.title + " " + noteTwo.note + " " + noteTwo.data
            }
        })

        val text = (if (noteOne.title.equals("Утро")) noteOne.note else "Нет дел!")
        Log.d("myLog","${text.toString()}")
        Log.d("myLog","${DataBase.getWhen(DayEnum.MORNING)}")
        DataBase.getFor()
    }
}