package ru.geekbrains.kotlin.view.details

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.kotlin.databinding.FragmentDetailsBinding
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
                with(binding) {
                    mainView.visibility = View.VISIBLE
                    cityName.text = weather.city.name
                    cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
                    temperatureValue.text = weather.temperature.toString()
                    feelsLikeValue.text = weather.feelsLike.toString()
                }
            }
            is DetailsState.Loading -> {

            }
            is DetailsState.Error -> binding.detailsFragment.showSnackBar("Ошибка!", 0)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}

