package com.ooredoo.currentweather

import android.app.Application
import com.location.openweathermap.core.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class OpenWeatherMapApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@OpenWeatherMapApp)
           // workManagerFactory()
            modules(appModule)
        }
    }
}
