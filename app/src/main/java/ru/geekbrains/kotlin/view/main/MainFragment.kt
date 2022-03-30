package ru.geekbrains.kotlin.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.geekbrains.kotlin.R
import ru.geekbrains.kotlin.databinding.FragmentMainBinding
import ru.geekbrains.kotlin.viewmodel.MainViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    lateinit var binding: FragmentMainBinding

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_main, container, false)
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvName.text = "Binding GOOD!"

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = object : Observer<Any> {
            override fun onChanged(data: Any) {
                renderData(data)
            }
        }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getWeather()
    }

    override fun onDestroy() {
        super.onDestroy()
       // binding = null
    }

    private fun renderData(data: Any) {
        Toast.makeText(context, "Good!", Toast.LENGTH_SHORT).show()
    }
}