package com.rk.weather.ui.main.bookmark

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rk.weather.data.db.WeatherDatabase
import com.rk.weather.data.db.entity.BookmarkEntity
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class BookmarkViewModel : ViewModel() {
    var item = ObservableField<BookmarkEntity>()
    private var mDisposable: Disposable? = null

    fun saveBookmark(weatherDatabase: WeatherDatabase, bookmarkEntity: BookmarkEntity) {
        Completable
            .fromCallable {
                weatherDatabase.bookmarkDao().insertBookmark(bookmarkEntity)
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

    fun getBookmarkList(weatherDatabase: WeatherDatabase): LiveData<MutableList<BookmarkEntity>> {
        return weatherDatabase.bookmarkDao().getBookmarkList()
    }

    fun deleteBookmark(weatherDatabase: WeatherDatabase, bookmarkEntity: BookmarkEntity) {
        Completable
            .fromCallable {
                weatherDatabase.bookmarkDao().deleteBookmark(bookmarkEntity)
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