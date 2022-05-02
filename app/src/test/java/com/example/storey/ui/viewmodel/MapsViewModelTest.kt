package com.example.storey.ui.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storey.data.local.statics.StaticData
import com.example.storey.getOrAwaitValue
import com.example.storey.utils.MainCoroutineRule
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var mapsViewModel: MapsViewModel
    private val lat = StaticData.LATITUDE_IN
    private val lon = StaticData.LONGITUDE_IN
    private val dummyLatLng = LatLng(lat, lon)

    @Before
    fun before() {
        mapsViewModel = MapsViewModel()
    }


    @Test
    fun `when get location address should not null`() = runBlockingTest {
        mapsViewModel.setAddress(Mockito.mock(Context::class.java), dummyLatLng)
        val location = mapsViewModel.getAddress.getOrAwaitValue()
        Assert.assertNotNull(location)
    }

    @Test
    fun `when set latLng should observe getLatLng`() = runBlockingTest {
        mapsViewModel.setLatLng(dummyLatLng)
        val actualLatLng = mapsViewModel.getLatLng.getOrAwaitValue()

        assertEquals(dummyLatLng, actualLatLng)
    }
}