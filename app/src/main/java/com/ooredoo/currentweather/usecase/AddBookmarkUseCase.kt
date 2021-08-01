package com.location.openweathermap.usecase

import com.location.openweathermap.core.database.dao.BookmarkDao
import com.location.openweathermap.core.database.entities.Bookmark
import com.location.openweathermap.model.domain.LocationWeatherModel

class AddBookmarkUseCase(private val dao: BookmarkDao) {

    suspend fun execute(locationWeather: LocationWeatherModel) = dao.insert(
        Bookmark(
            id = locationWeather.id,
            latitude = locationWeather.lat,
            longitude = locationWeather.lon,
            name = locationWeather.name,
            tempMax = locationWeather.tempMax,
            tempMin= locationWeather.tempMin,
            weatherDescription = locationWeather.weatherDescription,
            temp = locationWeather.temp,
            humidity = locationWeather.humidity,
            feelsLike = locationWeather.feelsLike,
            windSpeed = locationWeather.windSpeed!!,
            pressure = locationWeather.pressure,
            clouds = locationWeather.clouds
        )
    )

    suspend fun clearBookMarks() = dao.deleteAll()

}
