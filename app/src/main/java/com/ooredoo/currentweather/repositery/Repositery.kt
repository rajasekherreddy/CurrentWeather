package com.connect.repositery

import com.location.openweathermap.model.dto.CityNetworkDto
import com.ooredoo.new.RetrofitInstance
import retrofit2.Response

class Repositery {

    suspend fun getCurrentWeather(lat: Double,lng: Double): Response<CityNetworkDto>
    = RetrofitInstance.api.getCurrentWeather(lat,lng)
}