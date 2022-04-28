package ru.geekbrains.kotlin.view.weatherList

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.kotlin.R
import ru.geekbrains.kotlin.databinding.FragmentMainBinding
import ru.geekbrains.kotlin.history.HistoryFragment
import ru.geekbrains.kotlin.repository.Weather
import ru.geekbrains.kotlin.view.details.DetailsFragment
import ru.geekbrains.kotlin.view.utlis.IS_WORLD_KEY
import ru.geekbrains.kotlin.view.utlis.KEY_BUNDLE_WEATHER
import ru.geekbrains.kotlin.viewmodel.AppState
import ru.geekbrains.kotlin.viewmodel.MainViewModel

class WeatherListFragment : Fragment(), OnItemClickListener {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val adapter = WeatherListAdapter(this)

    private lateinit var viewModel: MainViewModel
    private var isRus: Boolean = false

    override fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                    putParcelable(KEY_BUNDLE_WEATHER, weather)
                })).addToBackStack("").commit()
    }

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
        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            mainFragmentFAB.setOnClickListener {
                changeWeatherDataSet()
            }
        }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java).also {
            it.getData().observe(viewLifecycleOwner, Observer { data: AppState -> renderData(data) })
            it.getWeatherFromLocalRus()
        }

        activity?.let {
            if (it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_WORLD_KEY, false)) {
                changeWeatherDataSet()
            } else
                viewModel.getWeatherFromLocalRus()
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun renderData(data: AppState) {
        with(binding) {
            when (data) {
                is AppState.Error -> {
                    loadingLayout.visibility = View.GONE
                    mainFragment.showSnackBar("Не получилось ${data.error} RemoteServer", 0)
                }
                is AppState.Loading -> {
                    loadingLayout.visibility = View.VISIBLE
                }
                is AppState.Success -> {
                    loadingLayout.visibility = View.GONE
                    adapter.setData(data.weatherData)
                }
            }
        }
    }

    private fun View.showSnackBar(
            text: String,
            length: Int = Snackbar.LENGTH_INDEFINITE) {
        Snackbar.make(this, text, length).show()
    }

    private fun View.showSnackBarAction(
            text: String,
            length: Int = Snackbar.LENGTH_INDEFINITE,
            actionText: String,
            action: (View) -> Unit) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.local_server ->
                binding.mainFragment.showSnackBarAction(getString(R.string.local_server),0, getString(R.string.open),
                        {changeWeatherDataSet()})
            R.id.server ->
                binding.mainFragment.showSnackBarAction(getString(R.string.remote_server),0, getString(R.string.open),
                        { viewModel.getWeatherFromServer()})
            R.id.history ->
                requireActivity().supportFragmentManager.apply {
                    beginTransaction().add(R.id.container, HistoryFragment.newInstance()).addToBackStack("").commit()
                }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeWeatherDataSet() {
        with(viewModel) {
            if (isRus) {
                getWeatherFromLocalRus()
                binding.mainFragmentFAB.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_rus))
            } else {
                getWeatherFromLocalWorld()
                binding.mainFragmentFAB.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_world))
            }
        }
        isRus = !isRus

        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()){
                putBoolean(IS_WORLD_KEY,isRus)
                apply()
            }
        }
    }

}