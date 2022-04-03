package ru.geekbrains.kotlin.view.weatherList

import android.os.Bundle
import android.view.*
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

class WeatherListFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private var isRus: Boolean = true

    private val adapter = WeatherListAdapter(object : OnItemClickListener{
        override fun onItemClick(weather: Weather) {
            val bundle = Bundle()
            bundle.putParcelable(DetailsFragment.KEY_BUNDLE_WEATHER, weather)
            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance(bundle)) //add не открывает, не могу понять почему
                    .addToBackStack("").commit()
        }
    })

    companion object {
        fun newInstance() = WeatherListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding.recyclerView.adapter = adapter
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getWeatherFromLocalRus()

        binding.mainFragmentFAB.setOnClickListener {
            isRus = !isRus
            changeWeatherDataSet()
        }
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.local_server ->
                changeWeatherDataSet()
            R.id.server ->
                viewModel.getWeatherFromServer()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeWeatherDataSet(){
        if (isRus) {
            viewModel.getWeatherFromLocalRus()
            binding.mainFragmentFAB.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_rus))
        } else {
            viewModel.getWeatherFromLocalWorld()
            binding.mainFragmentFAB.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_world))
        }
    }

}