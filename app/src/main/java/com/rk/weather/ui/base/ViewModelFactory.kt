package com.rk.weather.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rk.weather.data.api.ApiHelper
import com.rk.weather.data.repository.CurrentWeatherRepository
import com.rk.weather.ui.main.dashboard.DashBoardViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashBoardViewModel::class.java)) {
            return DashBoardViewModel(CurrentWeatherRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}