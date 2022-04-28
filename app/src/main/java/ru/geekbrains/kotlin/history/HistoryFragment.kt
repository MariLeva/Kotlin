package ru.geekbrains.kotlin.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import ru.geekbrains.kotlin.R
import ru.geekbrains.kotlin.databinding.FragmentHistoryBinding
import ru.geekbrains.kotlin.viewmodel.AppState
import ru.geekbrains.kotlin.viewmodel.HistoryViewModel


class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }
    private val adapter = HistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.historyRecyclerView.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.getData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getAllHistory()
    }

    private fun renderData(data: AppState){
        when (data){
            is AppState.Error -> {
                binding.historyRecyclerView.visibility = View.VISIBLE
                binding.loadingLayout.loadingLayout.visibility = View.GONE
                //
            }
            is AppState.Loading -> {
                binding.historyRecyclerView.visibility = View.GONE
                binding.loadingLayout.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.historyRecyclerView.visibility = View.VISIBLE
                binding.loadingLayout.loadingLayout.visibility = View.GONE
                adapter.setData(data.weatherData)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}