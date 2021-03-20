package com.rk.weather.ui.main.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rk.weather.data.model.ListItem
import com.rk.weather.databinding.ItemForecastBinding

class ForecastAdapter(
    val list: List<ListItem>,
    private val callBack: (ListItem, View, View, View, View, View) -> Unit
) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemForecastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listItem: ListItem) {
            binding.viewModel?.item?.set(listItem)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBinding =
            ItemForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mBinding.viewModel = ForecastItemViewModel()
        mBinding.rootView.setOnClickListener {
            mBinding.viewModel?.item?.get()?.let {
                callBack.invoke(
                    it,
                    mBinding.cardView,
                    mBinding.imageViewForecastIcon,
                    mBinding.textViewDayOfWeek,
                    mBinding.textViewTemp,
                    mBinding.linearLayoutTempMaxMin
                )
            }
        }
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list.get(position))
    }

    override fun getItemCount(): Int = list.size
}