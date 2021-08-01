package com.ooredoo

import android.content.Context
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.location.openweathermap.model.domain.LocationWeatherModel
import com.location.openweathermap.model.dto.CityNetworkDto
import com.location.openweathermap.usecase.AddBookmarkUseCase
import com.location.openweathermap.usecase.GetWeatherUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class WeatherWorkManager(

    context: Context, params: WorkerParameters
) :
    CoroutineWorker(context, params), KoinComponent {

    val getWeatherUseCase: GetWeatherUseCase by inject()
    val addBookmarkUseCase: AddBookmarkUseCase by inject()
    lateinit var context: Context

    init {
        this.context = context
    }

    override suspend fun doWork(): Result {

        val latitude = inputData.getDouble("latitude", 17.4344)
        val longnitude = inputData.getDouble("longnitude", 80.9931)
        loadBookmarks(latitude, longnitude)


        return Result.success()
    }


    suspend fun loadBookmarks(lat: Double, lon: Double) {
        getWeatherUseCase.execute(lat, lon)?.toUiModel()?.let { bookmarkLocation(it) }

    }

    suspend fun bookmarkLocation(locationWeather: LocationWeatherModel) {
        addBookmarkUseCase.clearBookMarks()
        addBookmarkUseCase.execute(locationWeather)


        withContext(Dispatchers.Main) {
            Toast.makeText(applicationContext, "Work manager executed", Toast.LENGTH_SHORT).show()
        }
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
}

