package com.rk.weather.ui.main.weatherdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rk.weather.data.model.ListItem
import com.rk.weather.databinding.ItemWeatherHourOfDayBinding

class WeatherHourOfDayAdapter(
    private val list: List<ListItem>,
    private val callBack: (ListItem) -> Unit
) : RecyclerView.Adapter<WeatherHourOfDayAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemWeatherHourOfDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listItem: ListItem) {
            binding.viewModel?.item?.set(listItem)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBinding = ItemWeatherHourOfDayBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewModel = WeatherHourOfDayItemViewModel()
        mBinding.viewModel = viewModel

        mBinding.rootView.setOnClickListener {
            mBinding.viewModel?.item?.get()?.let {
                callBack.invoke(it)
            }
        }
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list.get(position))
    }

    override fun getItemCount(): Int = list.size
}
