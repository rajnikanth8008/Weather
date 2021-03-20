package com.rk.weather.ui.main.weatherdetails

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rk.weather.data.db.WeatherDatabase
import com.rk.weather.data.db.entity.BookmarkEntity
import com.rk.weather.data.db.entity.ForecastEntity
import com.rk.weather.data.model.ListItem

class WeatherDetailsViewModel : ViewModel() {
    var weatherItem = ObservableField<ListItem>()
    var selectedDayDate: String? = null
    var selectedDayForecastLiveData: MutableLiveData<List<ListItem>> = MutableLiveData()

    fun getForecastData(weatherDatabase: WeatherDatabase): LiveData<ForecastEntity> {
        return weatherDatabase.forecastDao().getForecastData()
    }
}