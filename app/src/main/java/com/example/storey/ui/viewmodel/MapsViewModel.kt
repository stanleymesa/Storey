package com.example.storey.ui.viewmodel

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storey.data.local.statics.StaticData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.util.*

class MapsViewModel : ViewModel() {
    private val _getAddress = MutableLiveData<String>()
    val getAddress: LiveData<String> = _getAddress

    private val _getLatLng = MutableLiveData<LatLng>()
    val getLatLng: LiveData<LatLng> = _getLatLng

    fun setAddress(context: Context, latLng: LatLng) {
        viewModelScope.launch {
            val geocoder = Geocoder(context, Locale.getDefault())

            try {
                val location = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    1
                )
                _getAddress.postValue(location[0].getAddressLine(0))

            } catch (ex: Exception) {
                _getAddress.postValue(StaticData.LOCATION_ERROR)
            }
        }
    }

    fun setLatLng(latLng: LatLng) {
        _getLatLng.postValue(latLng)
    }

}