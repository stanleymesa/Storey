package com.example.storey.ui.viewmodel

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storey.data.local.statics.StaticData
import kotlinx.coroutines.launch
import java.util.*

class DetailViewModel : ViewModel() {
    private val _location = MutableLiveData<String>()
    val location: LiveData<String> = _location

    fun getLocation(context: Context, lat: Double, lon: Double) {
        viewModelScope.launch {
            val geocoder = Geocoder(context, Locale.getDefault())

            try {
                val location = geocoder.getFromLocation(
                    lat,
                    lon,
                    1
                )
                _location.postValue(location[0].getAddressLine(0))

            } catch (ex: Exception) {
                _location.postValue(StaticData.LOCATION_ERROR)
            }
        }
    }
}