package ru.geekbrains.kotlin.view.maps

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ru.geekbrains.kotlin.R
import ru.geekbrains.kotlin.databinding.FragmentMapsBinding
import ru.geekbrains.kotlin.databinding.FragmentMapsMainBinding

class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        val tomsk = LatLng(56.4977100, 84.9743700)
        googleMap.addMarker(MarkerOptions().position(tomsk).title("Marker in Tomsk"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(tomsk))
    }

    private var _binding: FragmentMapsMainBinding? = null
    private val binding: FragmentMapsMainBinding get() {
        return _binding!!
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}