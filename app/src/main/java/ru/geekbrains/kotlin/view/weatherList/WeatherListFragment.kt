package ru.geekbrains.kotlin.view.weatherList

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentProvider
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
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
import ru.geekbrains.kotlin.repository.City
import ru.geekbrains.kotlin.repository.Weather
import ru.geekbrains.kotlin.view.contentProvider.ContentProviderFragment
import ru.geekbrains.kotlin.view.details.DetailsFragment
import ru.geekbrains.kotlin.view.maps.MapsFragment
import ru.geekbrains.kotlin.view.utlis.IS_WORLD_KEY
import ru.geekbrains.kotlin.view.utlis.KEY_BUNDLE_WEATHER
import ru.geekbrains.kotlin.view.utlis.REQUEST_CODE
import ru.geekbrains.kotlin.view.utlis.REQUEST_CODE_LOCATION
import ru.geekbrains.kotlin.viewmodel.AppState
import ru.geekbrains.kotlin.viewmodel.MainViewModel
import java.util.*

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            mainFragmentFABLocation.setOnClickListener {
                checkPermission()
            }
        }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java).also {
            it.getData()
                .observe(viewLifecycleOwner, Observer { data: AppState -> renderData(data) })
            //  it.getWeatherFromLocalRus()
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
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, length).show()
    }

    private fun View.showSnackBarAction(
        text: String,
        length: Int = Snackbar.LENGTH_INDEFINITE,
        actionText: String,
        action: (View) -> Unit
    ) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.local_server ->
                binding.mainFragment.showSnackBarAction(getString(R.string.local_server),
                    0,
                    getString(R.string.open),
                    { changeWeatherDataSet() })
            R.id.server ->
                binding.mainFragment.showSnackBarAction(getString(R.string.remote_server),
                    0,
                    getString(R.string.open),
                    { viewModel.getWeatherFromServer() })
            R.id.history ->
                requireActivity().supportFragmentManager.apply {
                    if (findFragmentByTag("HistoryFragment") == null) {
                        beginTransaction().add(R.id.container, HistoryFragment.newInstance(), "HistoryFragment"
                        ).addToBackStack("").commit()
                    }
                }
            R.id.content_provider ->
                requireActivity().supportFragmentManager.apply {
                    if (findFragmentByTag("ContentProviderFragment") == null) {
                        beginTransaction().add(R.id.container, ContentProviderFragment.newInstance(), "ContentProviderFragment"
                        ).addToBackStack("").commit()
                    }
                }
            R.id.maps ->
                requireActivity().supportFragmentManager.apply {
                    if (findFragmentByTag("MapsFragment") == null) {
                        beginTransaction().add(R.id.container, MapsFragment(), "MapsFragment"
                        ).addToBackStack("").commit()
                    }
                }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeWeatherDataSet() {
        with(viewModel) {
            if (isRus) {
                getWeatherFromLocalRus()
                binding.mainFragmentFAB.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_rus
                    )
                )
            } else {
                getWeatherFromLocalWorld()
                binding.mainFragmentFAB.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_world
                    )
                )
            }
        }
        isRus = !isRus

        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                putBoolean(IS_WORLD_KEY, isRus)
                apply()
            }
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            explain()
        } else
            mRequestPermission()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        context?.let {
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val providerGPS = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                providerGPS?.let {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        100f,
                        locationListener
                    )
                }
            }
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            getAddressByLocation(location)
        }
    }

    private fun getAddressByLocation(location: Location) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        Thread {
            val addressText = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                10000000
            )[0].getAddressLine(0)
            requireActivity().runOnUiThread {
                showAddressDialog(addressText, location)
            }
        }.start()
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    onItemClick(
                        Weather(
                            City(
                                address, location.latitude, location.longitude
                            )
                        )
                    )
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun explain() {
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.dialog_rationale_title))
            .setMessage(resources.getString(R.string.dialog_rationale_message))
            .setPositiveButton(resources.getString(R.string.dialog_rationale_give_access)) { _, _ ->
                mRequestPermission()
            }
            .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun mRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else explain()
            }
        } else super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
