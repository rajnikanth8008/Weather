package com.rk.weather.ui.main.weatherdetails

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.rk.weather.data.model.ListItem

class WeatherHourOfDayItemViewModel : ViewModel() {
    var item = ObservableField<ListItem>()
}