package com.rk.weather.ui.main.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rk.weather.R
import com.rk.weather.data.api.ApiHelper
import com.rk.weather.data.api.ApiServiceImp
import com.rk.weather.data.db.WeatherDatabase
import com.rk.weather.data.db.entity.ForecastEntity
import com.rk.weather.data.model.ListItem
import com.rk.weather.databinding.DashBoardFragmentBinding
import com.rk.weather.ui.base.ViewModelFactory
import com.rk.weather.ui.main.MainActivity
import com.rk.weather.utills.*

@Suppress("DEPRECATION")
class DashBoardFragment : Fragment() {

    private lateinit var viewModel: DashBoardViewModel
    lateinit var binding: DashBoardFragmentBinding
    private lateinit var forecastList: List<ListItem>
    private lateinit var lat: String
    private lateinit var lon: String
    var weatherDatabase: WeatherDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this, ViewModelFactory(ApiHelper(ApiServiceImp.invoke())))
            .get(DashBoardViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.dash_board_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lat = (activity as MainActivity).sharedPreferences.getString(
            Constants.Coords.LAT,
            "17.385044"
        ) ?: "17.385044"
        lon = (activity as MainActivity).sharedPreferences.getString(
            Constants.Coords.LON,
            "78.486671"
        ) ?: "78.486671"
        weatherDatabase = context?.let { WeatherDatabase.getDatabase(it) }
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.getCurrentWeather(lat, lon).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data.let {
                        with(binding) {
                            binding.progressBar.hide()
                            binding.containerForecast.cardView.show()
                            containerForecast.viewState = it
                        }
                    }
                }
                Status.LOADING -> {
                    binding.progressBar.show()
                    binding.containerForecast.cardView.invisible()
                }
                Status.ERROR -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
        viewModel.getForecastWeather(lat, lon)
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let {
                            weatherDatabase?.let { it1 ->
                                viewModel.saveForecastData(
                                    it1,
                                    ForecastEntity(
                                        it.city.country,
                                        it.city.name,
                                        it.list,
                                        it.city.coord.lat,
                                        it.city.coord.lon
                                    )
                                )
                            }
                            initForecastAdapter(ForecastMapper().mapFrom(it.list))
                            (activity as MainActivity).binding.viewModel?.toolbarTitle?.set(it.city.getCityAndCountry())
                        }
                    }
                    Status.LOADING -> {
                        it.data.let { }
                    }
                    Status.ERROR -> {
                        it.data.let { }
                    }
                }
            })
    }

    private fun initForecastAdapter(list: List<ListItem>) {
        val adapter = ForecastAdapter(list) { item, cardView, forecastIcon, dayOfWeek, temp, tempMaxMin ->
            val action = DashBoardFragmentDirections.actionDashBoardFragmentToWeatherDetailsFragment(item)
            findNavController().navigate(action,
                FragmentNavigator.Extras.Builder()
                    .addSharedElements(
                        mapOf(
                            cardView to cardView.transitionName,
                            forecastIcon to forecastIcon.transitionName,
                            dayOfWeek to dayOfWeek.transitionName,
                            temp to temp.transitionName,
                            tempMaxMin to tempMaxMin.transitionName
                        )
                    )
                    .build()
            )
        }
        binding.recyclerForecast.adapter = adapter
        binding.recyclerForecast.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        postponeEnterTransition()
        binding.recyclerForecast.viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }
    }
}