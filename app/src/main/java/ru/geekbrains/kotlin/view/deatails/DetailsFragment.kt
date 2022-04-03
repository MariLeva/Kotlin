package ru.geekbrains.kotlin.view.deatails

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import ru.geekbrains.kotlin.databinding.FragmentDetailsBinding
import ru.geekbrains.kotlin.repository.Weather

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!


    companion object {
        const val KEY_BUNDLE_WEATHER = "weather"

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather: Weather = requireArguments().getParcelable<Weather>(KEY_BUNDLE_WEATHER)!!
        binding.cityName.text = weather.city.name
        binding.temperatureValue.text = weather.temperature.toString()
        binding.feelsLikeLabel.text = weather.feelsLike.toString()
        binding.cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}