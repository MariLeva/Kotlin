package ru.geekbrains.kotlin.view.details

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import ru.geekbrains.kotlin.databinding.FragmentDetailsBinding
import ru.geekbrains.kotlin.repository.Weather
import ru.geekbrains.kotlin.view.utlis.KEY_BUNDLE_WEATHER

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            with(binding){
                cityName.text = it.city.name
                temperatureValue.text = it.temperature.toString()
                feelsLikeLabel.text = it.feelsLike.toString()
                cityCoordinates.text = "${it.city.lat} ${it.city.lon}"
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}