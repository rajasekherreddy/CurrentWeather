package com.location.openweathermap.model.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "bookmarks")
data class CityNetworkDto(
    @PrimaryKey
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("coord")
    var coord: CoordNetworkDto,
    @SerializedName("main")
    var forecastMain: ForecastMainNetworkDto,
    @SerializedName("dt")
    var unixDateTime: Long,
    @SerializedName("wind")
    var wind: WindNetworkDto?,
    @SerializedName("rain")
    var rain: RainNetworkDto?,
    @SerializedName("snow")
    var snow: SnowNetworkDto?,
    @SerializedName("weather")
    var weather: List<WeatherNetworkDto>,
    @SerializedName("sys")
    var sys: ApiSysObjectNetworkDto?,
    @SerializedName("clouds")
    var clouds: CloudsNetworkDto?
)
