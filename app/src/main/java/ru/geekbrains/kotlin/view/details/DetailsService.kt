package ru.geekbrains.kotlin.view.details

import android.app.IntentService
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import ru.geekbrains.kotlin.BuildConfig
import ru.geekbrains.kotlin.repository.WeatherDTO
import ru.geekbrains.kotlin.view.utlis.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailsService(val name: String = "") : IntentService(name) {
    private val message = Intent(KEY_WAVE_SERVICE_BROADCAST)

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val lon = it.getDoubleExtra(KEY_BUNDLE_LON, 0.0)
            val lat = it.getDoubleExtra(KEY_BUNDLE_LAT, 0.0)
            loadWeather(lon.toString(), lat.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadWeather(lon: String, lat: String) {
        try {
            val uri = URL("${YA_DOMAIN}${YA_ENDPOINT}lat=${lat}&lon=${lon}")
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = (uri.openConnection() as HttpsURLConnection).apply {
                    requestMethod = "GET"
                    addRequestProperty(
                        YA_API_KEY,
                        BuildConfig.WEATHER_API_KEY
                    )
                    readTimeout = 1000
                }

                val bufferedReader =
                    BufferedReader(InputStreamReader(urlConnection.inputStream))

                val weatherDTO: WeatherDTO =
                    Gson().fromJson(bufferedReader, WeatherDTO::class.java)

                message.putExtra(DETAILS_RESULT, weatherDTO)
                LocalBroadcastManager.getInstance(this).sendBroadcast(message)

            } catch (e: Exception) {
                messagePutExtraError("Error empty")
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            messagePutExtraError("Error MalformedURLException")
        }
    }

    private fun messagePutExtraError(s: String) {
        message.putExtra(DETAILS_RESULT_ERROR, s)
        LocalBroadcastManager.getInstance(this).sendBroadcast(message)
    }
}