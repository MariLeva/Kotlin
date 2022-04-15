package ru.geekbrains.kotlin.view.details

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.kotlin.R
import ru.geekbrains.kotlin.databinding.FragmentDetailsBinding
import ru.geekbrains.kotlin.repository.Weather
import ru.geekbrains.kotlin.repository.WeatherDTO
import ru.geekbrains.kotlin.repository.WeatherLoader
import ru.geekbrains.kotlin.view.utlis.KEY_BUNDLE_WEATHER
import ru.geekbrains.kotlin.view.weatherList.WeatherListFragment

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weather: Weather
    private val onLoadListener: WeatherLoader.WeatherLoaderListener =
        object : WeatherLoader.WeatherLoaderListener{
            override fun onLoaded(weatherDTO: WeatherDTO) {
                displayWeather(weatherDTO)
            }
            override fun onFailed(throwable: Throwable) {
                binding.detailsFragment.showSnackBar("Ошибка загрузки!",0)
            }

        }

    private fun View.showSnackBar(
        text: String,
        length: Int = Snackbar.LENGTH_INDEFINITE) {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weather = arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?: Weather()
        binding.mainView.visibility = View.GONE
        val loader = WeatherLoader(onLoadListener, weather.city.lat, weather.city.lon)
        loader.loadWeather()
    }


    private fun displayWeather(weatherDTO: WeatherDTO){
        with(binding){
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
        super.onDestroy()
    }


}