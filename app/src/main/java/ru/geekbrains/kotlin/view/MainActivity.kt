package ru.geekbrains.kotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
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
    }
}