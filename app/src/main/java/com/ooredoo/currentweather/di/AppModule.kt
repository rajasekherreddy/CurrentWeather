package com.location.openweathermap.core.di

import androidx.room.Room
import com.connect.repositery.Repositery
import com.location.openweathermap.map.viewmodel.MapViewModel
import com.location.openweathermap.usecase.AddBookmarkUseCase
import com.location.openweathermap.usecase.GetBookmarksUseCase
import com.location.openweathermap.usecase.GetWeatherUseCase
import com.ooredoo.WeatherWorkManager
import com.ooredoo.currentweather.database.WeatherDatabase
import com.ooredoo.new.RetrofitInstance
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(androidApplication(), WeatherDatabase::class.java, "WEATHER_DB")
            .build()
    }
    single { get<WeatherDatabase>().bookmarkDao }



    single {
        RetrofitInstance
    }
    single {
        Repositery()
    }

    single {
        GetBookmarksUseCase(get())
    }

    single {
        GetWeatherUseCase(get())
    }

    single {
        AddBookmarkUseCase(get())
    }



    viewModel { MapViewModel(get(), get(), get(), get()) }

    worker { WeatherWorkManager(get(), get()) }

}
