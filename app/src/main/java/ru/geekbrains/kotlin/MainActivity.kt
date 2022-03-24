package ru.geekbrains.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                findViewById<TextView>(R.id.textView).text = "Нажата кнопка!"
            }
        })


    }
}