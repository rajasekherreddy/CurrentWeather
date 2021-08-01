package com.ooredoo.currentweather.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.location.openweathermap.core.database.dao.BookmarkDao
import com.location.openweathermap.core.database.entities.Bookmark
import com.location.openweathermap.model.dto.CityNetworkDto

@Database(entities = [Bookmark::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract val bookmarkDao: BookmarkDao

}
