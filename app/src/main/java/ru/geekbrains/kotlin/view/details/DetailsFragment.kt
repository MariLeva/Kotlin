package ru.geekbrains.kotlin.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.kotlin.databinding.FragmentDetailsBinding
import ru.geekbrains.kotlin.repository.Weather
import ru.geekbrains.kotlin.repository.WeatherDTO
import ru.geekbrains.kotlin.repository.WeatherLoader
import ru.geekbrains.kotlin.view.utlis.*

class DetailsFragment : Fragment(), WeatherLoader {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weather: Weather

    private fun View.showSnackBar(
        text: String,
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, length).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver,
            IntentFilter(KEY_WAVE_SERVICE_BROADCAST)
        )
        weather = arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER) ?: Weather()
        binding.mainView.visibility = View.GONE
        requireActivity().startService(Intent(requireContext(),DetailsService::class.java).apply {
            putExtra(KEY_BUNDLE_LAT,weather.city.lat)
            putExtra(KEY_BUNDLE_LON,weather.city.lon)
        })
    }


    private fun displayWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            val city = weather.city
            cityName.text = city.name
            cityCoordinates.text = "${city.lat} ${city.lon}"
            weatherCondition.text = weatherDTO.fact?.condition
            temperatureValue.text = weatherDTO.fact?.temp.toString()
            feelsLikeValue.text = weatherDTO.fact?.feels_like.toString()
        }
    }

    override fun onDestroy() {
        _binding = null
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
        super.onDestroy()
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let { intent ->
                intent.getParcelableExtra<WeatherDTO>(DETAILS_RESULT)?.let {
                    onLoaded(it)
                }
                intent.getStringExtra(DETAILS_RESULT_ERROR)?.let {
                    onFailed(it)
                }
            }
        }
    }

    override fun onLoaded(weatherDTO: WeatherDTO) {
        displayWeather(weatherDTO)
    }

    override fun onFailed(s: String) {
        binding.detailsFragment.showSnackBar(s, 0)
    }

}

