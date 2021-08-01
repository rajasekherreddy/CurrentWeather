package com.ooredoo.currentweather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.location.openweathermap.map.viewmodel.MapViewModel
import com.ooredoo.currentweather.databinding.ActivityMainBinding
import com.ooredoo.currentweather.utils.ActivityResultHandler
import org.koin.androidx.viewmodel.ext.android.viewModel


class WeatherActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val mViewmodel: MapViewModel by viewModel()
    private var permissionLauncher: ActivityResultHandler<Array<String>, Map<String, Boolean>>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        permissionLauncher = ActivityResultHandler.registerPermissionForResult(this)

        binding.model = mViewmodel

    }

    override fun onResume() {
        super.onResume()

        mViewmodel.bookmarksWeatherLiveData.observe(this, { mResponse ->
            binding.locationWeather = mResponse

        })
        val array = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissionLauncher?.launch(array) { result ->
            if (allPermissionGranted(result)){
                val loc= getLastKnownLocation(this)
                loc?.let { mViewmodel.loadBookmarks(it.latitude,loc.longitude) }
            }
        }

    }



    private fun allPermissionGranted(result: Map<String, Boolean>): Boolean {
        for (permission in result) {
            if (!permission.value) {
                return false
            }
        }
        return true
    }
    companion object @SuppressLint("MissingPermission")
    internal fun getLastKnownLocation(context: Context): Location? {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = locationManager.getProviders(true)
        var location: Location? = null
        for (i in providers.size - 1 downTo 0) {
            location = locationManager.getLastKnownLocation(providers[i])
            if (location != null)
                break
        }

        return location
    }
}