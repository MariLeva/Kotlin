package ru.geekbrains.kotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import ru.geekbrains.kotlin.DataBase
import ru.geekbrains.kotlin.DayEnum
import ru.geekbrains.kotlin.Note
import ru.geekbrains.kotlin.R
import ru.geekbrains.kotlin.view.main.MainFragment
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance()).commit();
        }

        /*val noteOne = Note("Утро", "Прогулка с собакой", Date())
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
        DataBase.getFor()*/
    }
}