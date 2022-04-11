package ru.geekbrains.kotlin.view.weatherList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.geekbrains.kotlin.databinding.FragmentMainRecyclerItemBinding
import ru.geekbrains.kotlin.repository.Weather

class WeatherListAdapter(
        private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<WeatherListAdapter.CityHolder>() {

    private var data: List<Weather> = listOf()

    fun setData(weather: List<Weather>){
        this.data = weather
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
            FragmentMainRecyclerItemBinding.bind(itemView).apply {
                mainFragmentRecyclerItemTextView.text = weather.city.name
                root.setOnClickListener{
                    onItemClickListener.onItemClick(weather)
                }
            }
        }
    }
}