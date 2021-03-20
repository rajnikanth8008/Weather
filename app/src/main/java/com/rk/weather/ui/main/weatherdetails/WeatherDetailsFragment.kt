package com.rk.weather.ui.main.weatherdetails

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rk.weather.R
import com.rk.weather.data.db.WeatherDatabase
import com.rk.weather.data.model.ListItem
import com.rk.weather.databinding.WeatherDetailsFragmentBinding

class WeatherDetailsFragment : Fragment() {

    private lateinit var viewModel: WeatherDetailsViewModel
    private lateinit var binding: WeatherDetailsFragmentBinding
    private val weatherDetailFragmentArgs: WeatherDetailsFragmentArgs by navArgs()
    var weatherDatabase: WeatherDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.weather_details_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherDetailsViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.weatherItem.set(weatherDetailFragmentArgs.weatherItem)
        viewModel.selectedDayDate = weatherDetailFragmentArgs.weatherItem.dtTxt?.substringBefore(" ")
        weatherDatabase = context?.let { WeatherDatabase.getDatabase(it) }
        weatherDatabase?.let {
            viewModel.getForecastData(it).observe(viewLifecycleOwner) {
                viewModel.selectedDayForecastLiveData
                    .postValue(
                        it.list.filter { item ->
                            item.dtTxt?.substringBefore(" ") == viewModel.selectedDayDate
                        }
                    )
            }
        }

        viewModel.selectedDayForecastLiveData.observe(viewLifecycleOwner) {
            initWeatherHourOfDayAdapter(it)
        }

        binding.fabClose.setOnClickListener {
            findNavController().popBackStack()
        }
        val inflateTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = inflateTransition
    }

    private fun initWeatherHourOfDayAdapter(list: List<ListItem>) {
        val adapter = WeatherHourOfDayAdapter(list) { item -> }
        binding.recyclerViewHourOfDay.adapter = adapter
    }
}