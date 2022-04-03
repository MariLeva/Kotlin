package ru.geekbrains.kotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.geekbrains.kotlin.R
import ru.geekbrains.kotlin.view.weatherList.WeatherListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction().replace(R.id.container, WeatherListFragment.newInstance()).commit();
        }
    }
}