package com.location.openweathermap.map.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.location.openweathermap.core.database.entities.Bookmark
import com.location.openweathermap.model.domain.LocationWeatherModel
import com.location.openweathermap.model.dto.CityNetworkDto
import com.location.openweathermap.usecase.AddBookmarkUseCase
import com.location.openweathermap.usecase.GetBookmarksUseCase
import com.location.openweathermap.usecase.GetWeatherUseCase
import com.ooredoo.WeatherWorkManager
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import java.util.concurrent.TimeUnit

@KoinApiExtension
class MapViewModel(
    private var aplication: Application,
    private var getWeatherUseCase: GetWeatherUseCase,
    private var addBookmarkUseCase: AddBookmarkUseCase,
    private var getBookmarksUseCase: GetBookmarksUseCase,

    ) : AndroidViewModel(aplication) {
    companion object {
        const val WORK_NAME = "com.example.android.work335.RefreshDataWorker"
    }

    var bookmarksWeatherLiveData = MutableLiveData<LocationWeatherModel>()


    fun bookmarkLocation(locationWeather: LocationWeatherModel) {
        viewModelScope.launch {
            addBookmarkUseCase.clearBookMarks()
            addBookmarkUseCase.execute(locationWeather)
        }
    }


    fun loadBookmarks(lat: Double, lon: Double) {
        viewModelScope.launch {
            if (isNetworkAvailable(aplication) && getBookmarksUseCase.getBookMarks().isEmpty()) {

                val myData: Data = workDataOf("latitude" to lat,
                    "longnitude" to lon)

                val myPeriodicWorkRequest =
                    PeriodicWorkRequestBuilder<WeatherWorkManager>(15, TimeUnit.MINUTES)
                        .setInputData(myData)
                        .build()
             //    WorkManager.getInstance(aplication).enqueue(myPeriodicWorkRequest)
                WorkManager.getInstance(aplication).enqueueUniquePeriodicWork(
                    WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, myPeriodicWorkRequest
                )

                WorkManager.getInstance(aplication)
                    .getWorkInfoByIdLiveData(myPeriodicWorkRequest.id)
                    .observeForever(Observer {
                        it?.let {
                            if (it.state == WorkInfo.State.ENQUEUED) {
                                viewModelScope.launch {
                                    launchFromDatabase()
                                }
                                Log.e("ddd", "Download enqueued.")
                            } else if (it.state == WorkInfo.State.FAILED) {
                                Log.e("ddd", "Download Failed")
                            } else {
                                Log.e("ddd", "Download running.")
                            }
                        }
                    })

                // bookmarksWeatherLiveData.value = getWeatherUseCase.execute(lat, lon)?.toUiModel()
                //  bookmarksWeatherLiveData.value?.let { bookmarkLocation(it) }
            } else {
                launchFromDatabase()
            }
        }
    }

    suspend fun launchFromDatabase() {
        try {
            bookmarksWeatherLiveData.value = getBookmarksUseCase.getBookMarks().get(0).toUiModel()

        } catch (e: Exception) {
            Toast.makeText(aplication, "No Local data available. ", Toast.LENGTH_LONG).show()
        }
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    private fun CityNetworkDto.toUiModel() = LocationWeatherModel(
        id = this.id,
        name = this.name,
        lat = this.coord.lat,
        lon = this.coord.lon,
        weatherDescription = this.weather.firstOrNull()?.description!!,
        windSpeed = this.wind?.speed,
        temp = this.forecastMain.temp.toInt(),
        tempMax = this.forecastMain.tempMax.toInt(),
        tempMin = this.forecastMain.tempMin.toInt(),
        humidity = this.forecastMain.humidity,
        pressure = this.forecastMain.pressure,
        feelsLike = this.forecastMain.feelsLike.toInt(),
        clouds = this.clouds?.all ?: 0
    )

    private fun Bookmark.toUiModel() = LocationWeatherModel(
        id = this.id,
        name = this.name,
        lat = this.latitude,
        lon = this.longitude,
        weatherDescription = this.weatherDescription,
        windSpeed = this.windSpeed,
        temp = this.temp,
        tempMax = this.tempMax,
        tempMin = this.tempMin,
        humidity = this.humidity,
        pressure = this.pressure,
        feelsLike = this.feelsLike,
        clouds = this.clouds
    )


}
