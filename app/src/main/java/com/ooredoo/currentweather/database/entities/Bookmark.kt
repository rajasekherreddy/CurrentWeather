package com.location.openweathermap.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class Bookmark constructor(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "latitude") var latitude: Double,
    @ColumnInfo(name = "longitude") var longitude: Double,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "tempMax") var tempMax: Int,
    @ColumnInfo(name = "tempMin") var tempMin: Int,
    @ColumnInfo(name = "temp") var temp: Int,
    @ColumnInfo(name = "weatherDescription") var weatherDescription: String,
    @ColumnInfo(name = "humidity") var humidity: Int,
    @ColumnInfo(name = "feelsLike") var feelsLike: Int,
    @ColumnInfo(name = "windSpeed") var windSpeed: Double,
    @ColumnInfo(name = "pressure") var pressure: Int,
    @ColumnInfo(name = "clouds") var clouds: Int

)
