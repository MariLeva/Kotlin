package ru.geekbrains.kotlin.view.weatherList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.geekbrains.kotlin.databinding.FragmentMainRecyclerItemBinding
import ru.geekbrains.kotlin.repository.Weather

class WeatherListAdapter(
        private val onItemClickListener: OnItemClickListener,
        private var data: List<Weather> = listOf()
) : RecyclerView.Adapter<WeatherListAdapter.CityHolder>() {

    fun setData(weather: List<Weather>){
        data = weather
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val binding = FragmentMainRecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return CityHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weather: Weather){
            val binding = FragmentMainRecyclerItemBinding.bind(itemView)
            binding.mainFragmentRecyclerItemTextView.text = weather.city.name
            binding.root.setOnClickListener {
                onItemClickListener.onItemClick(weather)
            }
        }
    }
}