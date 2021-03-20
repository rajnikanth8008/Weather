package com.rk.weather.ui.main.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rk.weather.data.db.WeatherDatabase
import com.rk.weather.data.db.entity.ForecastEntity
import com.rk.weather.data.model.CurrentWeather
import com.rk.weather.data.model.ForecastWeather
import com.rk.weather.data.repository.CurrentWeatherRepository
import com.rk.weather.utills.Resource
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DashBoardViewModel(private val currentWeatherRepository: CurrentWeatherRepository) : ViewModel() {

    private val currentWeatherLiveData = MutableLiveData<Resource<CurrentWeather>>()
    private val forecastWeatherLiveData = MutableLiveData<Resource<ForecastWeather>>()
    private val compositeDisposable = CompositeDisposable()
    private var mDisposable: Disposable? = null

    private fun fetchCurrentLocation(
        latitude: String,
        longitude: String
    ): MutableLiveData<Resource<CurrentWeather>> {
        currentWeatherLiveData.postValue(Resource.loading(null))
        compositeDisposable.add(
            currentWeatherRepository.getCurrentWeather(latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    currentWeatherLiveData.postValue(Resource.success(it))
                }, {
                    currentWeatherLiveData.postValue(Resource.error("Something Went Wrong", null))
                })
        )
        return currentWeatherLiveData
    }

    private fun fetchForecastWeather(
        latitude: String,
        longitude: String
    ): MutableLiveData<Resource<ForecastWeather>> {
        forecastWeatherLiveData.postValue(Resource.loading(null))
        compositeDisposable.add(
            currentWeatherRepository.getForecastWeather(latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    forecastWeatherLiveData.postValue(Resource.success(it))
                }, {
                    Log.e("CurrentWeather Error:", it.toString())
                    forecastWeatherLiveData.postValue(Resource.error("Something Went Wrong", null))
                })
        )
        return forecastWeatherLiveData
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getCurrentWeather(latitude: String, longitude: String): LiveData<Resource<CurrentWeather>> {
        return fetchCurrentLocation(latitude, longitude)
    }

    fun getForecastWeather(
        latitude: String,
        longitude: String
    ): LiveData<Resource<ForecastWeather>> {
        return fetchForecastWeather(latitude, longitude)
    }

    fun saveForecastData(weatherDatabase: WeatherDatabase, forecastEntity: ForecastEntity) {
        Completable
            .fromCallable {
                weatherDatabase.forecastDao().deleteAndInsert(forecastEntity)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    if (!d.isDisposed) {
                        mDisposable = d
                    }
                }

                override fun onComplete() {
                    mDisposable!!.dispose()
                }

                override fun onError(e: Throwable) {
                    mDisposable!!.dispose()
                }
            })
    }
}