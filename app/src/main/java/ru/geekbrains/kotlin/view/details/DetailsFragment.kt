package ru.geekbrains.kotlin.view.details

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ru.geekbrains.kotlin.databinding.FragmentDetailsBinding
import ru.geekbrains.kotlin.repository.City
import ru.geekbrains.kotlin.repository.Weather
import ru.geekbrains.kotlin.view.utlis.*
import ru.geekbrains.kotlin.viewmodel.DetailsState
import ru.geekbrains.kotlin.viewmodel.DetailsViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weather: Weather

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, object : Observer<DetailsState> {
            override fun onChanged(t: DetailsState) {
                displayWeather(t)
            }

        })
        weather = arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER) ?: Weather()
        binding.mainView.visibility = View.GONE
        viewModel.getWeather(weather.city)
    }


    private fun displayWeather(detailsState: DetailsState) {
        when (detailsState) {
            is DetailsState.Success -> {
                val weather = detailsState.weather
                saveCity(weather.city, weather)
                with(binding) {
                    mainView.visibility = View.VISIBLE
                    cityName.text = weather.city.name
                    cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
                    temperatureValue.text = weather.temperature.toString()
                    feelsLikeValue.text = weather.feelsLike.toString()
                    weatherCondition.text = weather.condition
                    Glide.with(requireActivity())
                        .load("https://freepngimg.com/thumb/light/78413-neon-effect-creative-lighting-halo-glow.png")
                        .into(imgCityIcon)
                    Picasso.get()
                        ?.load("https://freepngimg.com/thumb/house/9-2-city-building-png.png")
                        ?.into(imgBottom)

                    iconCondition.loadSvg("https://yastatic.net/weather/i/icons/blueye/color/svg/${weather.icon}.svg")
                }
            }
            is DetailsState.Error -> binding.detailsFragment.showSnackBar(
                "Ошибка закгрузки данных!",
                0
            )
        }
    }

    fun ImageView.loadSvg(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
            .build()
        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()
        imageLoader.enqueue(request)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun saveCity(city: City, weather: Weather) {
        Thread {
            activity?.runOnUiThread {
                viewModel.saveCityToDB(
                    Weather(city, weather.temperature, weather.feelsLike, weather.condition, weather.icon))
            }
        }.start()
    }

}

