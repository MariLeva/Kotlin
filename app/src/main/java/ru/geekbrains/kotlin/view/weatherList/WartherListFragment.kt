package ru.geekbrains.kotlin.view.weatherList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.kotlin.R
import ru.geekbrains.kotlin.databinding.FragmentMainBinding
import ru.geekbrains.kotlin.repository.Weather
import ru.geekbrains.kotlin.view.deatails.DetailsFragment
import ru.geekbrains.kotlin.viewmodel.AppState
import ru.geekbrains.kotlin.viewmodel.MainViewModel

class WartherListFragment : Fragment(), OnItemClickListener {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private var isRus = true
    private val adapter = WeatherListAdapter(this)

    companion object {
        @JvmStatic
        fun newInstance() = WartherListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getWeatherFromLocalRus()

        binding.mainFragmentFAB.setOnClickListener {
            isRus = !isRus
            if (isRus) {
                viewModel.getWeatherFromLocalRus()
                binding.mainFragmentFAB.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_rus))
            } else {
                viewModel.getWeatherFromLocalWorld()
                binding.mainFragmentFAB.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_world))
            }
        }
    }


    override fun onItemClick(weather: Weather) {
        val bundle = Bundle()
        // bundle.putParcelable(KEY_BUNDLE_WEATHER, weather)
        //    requireActivity().supportFragmentManager.beginTransaction().add(
        //          R.id.container, DetailsFragment.newInstance(bundle)
        //).addToBackStack("").commit()
    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.root, "Не получилось ${data.error} RemoteServer", Snackbar.LENGTH_LONG).show()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setData(data.weatherData)
            }
        }
    }


}