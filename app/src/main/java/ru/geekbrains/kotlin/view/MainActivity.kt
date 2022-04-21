package ru.geekbrains.kotlin.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.geekbrains.kotlin.R
import ru.geekbrains.kotlin.databinding.ActivityMainBinding
import ru.geekbrains.kotlin.repository.MainBroadcastReceiver
import ru.geekbrains.kotlin.view.weatherList.WeatherListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val receiver = MainBroadcastReceiver()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        registerReceiver(receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance()).commit()
        }
    }
}
/* private var clickListener: View.OnClickListener = object : View.OnClickListener {
     override fun onClick(v: View?) {
         try {
             val uri = URL(binding.url.text.toString())
             val handler = Handler()
             Thread {
                 var urlConnection: HttpsURLConnection? = null
                 try {
                     urlConnection = uri.openConnection() as HttpsURLConnection
                     urlConnection.requestMethod = "GET"
                     urlConnection.readTimeout = 1000
                     val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                     val result = getLines(reader)
                     handler.post {
                         binding.webview.loadDataWithBaseURL(null, result, "text/html; charset=utf-8", "utf-8", null)
                     }
                 } catch (e: Exception) {
                     Log.e("", "Fail connection", e)
                     e.printStackTrace()
                 } finally {
                     urlConnection?.disconnect()
                 }
             }.start()
         } catch (e: MalformedURLException) {
             Log.e("", "Fail URI", e)
             e.printStackTrace()
         }
     }

     @RequiresApi(Build.VERSION_CODES.N)
     private fun getLines(reader: BufferedReader): String {
         return reader.lines().collect(Collectors.joining("\n"))
     }
 }}*/