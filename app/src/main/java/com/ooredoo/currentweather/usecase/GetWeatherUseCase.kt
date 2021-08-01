package com.location.openweathermap.usecase

import com.location.openweathermap.model.dto.CityNetworkDto
import com.ooredoo.new.RetrofitInstance

class GetWeatherUseCase(private val weatherApiService: RetrofitInstance) {

    suspend fun execute(lat: Double, lon: Double): CityNetworkDto? {
        val response = weatherApiService.api.getCurrentWeather(lat,lon,)
        return response.body()
    }

}
