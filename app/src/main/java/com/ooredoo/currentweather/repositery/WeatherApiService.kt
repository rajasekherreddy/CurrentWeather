package com.ooredoo.new

import com.location.openweathermap.model.dto.CityNetworkDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApiService {


    //@GET("weather?units=metric&APPID=fae7190d7e6433ec3a45285ffcf55c86")
    @GET("weather?units=metric&APPID=fae7190d7e6433ec3a45285ffcf55c86&lat=16.4344&lon=80.9931")
    suspend fun getCurrentWeather(): Response<CityNetworkDto>

    @GET("weather?units=metric&APPID=fae7190d7e6433ec3a45285ffcf55c86")
    suspend fun getCurrentWeather(@Query("lat") lat: Double, @Query("lon") lng: Double): Response<CityNetworkDto>
}