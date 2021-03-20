package com.rk.weather.ui.main.dashboard

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.rk.weather.data.model.ListItem

class ForecastItemViewModel : ViewModel() {
    var item = ObservableField<ListItem>()
}