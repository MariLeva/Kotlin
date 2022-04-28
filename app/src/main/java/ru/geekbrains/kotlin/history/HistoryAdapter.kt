package ru.geekbrains.kotlin.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import ru.geekbrains.kotlin.databinding.FragmentHistoryRecyclerItemBinding
import ru.geekbrains.kotlin.repository.Weather

class HistoryAdapter :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private var data: List<Weather> = arrayListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Weather) {
            FragmentHistoryRecyclerItemBinding.bind(itemView).apply {
                tvHistoryItemCity.text = data.city.name
                tvHistoryItemTemperature.text = data.temperature.toString()
                tvHistoryItemFeelsLike.text = data.feelsLike.toString()
                tvHistoryItemFeelsCondition.text = data.condition
                imgHistoryItemIcon.loadSvg("https://yastatic.net/weather/i/icons/blueye/color/svg/${data.icon}.svg")
            }
        }
    }

    fun ImageView.loadSvg(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
            .build()
        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()
        imageLoader.enqueue(request)
    }

    fun setData(data: List<Weather>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentHistoryRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount() = data.size
}