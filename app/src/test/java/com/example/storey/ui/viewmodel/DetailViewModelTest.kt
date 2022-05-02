package com.example.storey.ui.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storey.data.local.statics.StaticData
import com.example.storey.getOrAwaitValue
import com.example.storey.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var detailViewModel: DetailViewModel
    private val lat = StaticData.LATITUDE_IN
    private val lon = StaticData.LONGITUDE_IN

    @Before
    fun before() {
        detailViewModel = DetailViewModel()
    }

    @Test
    fun `when get location address should not null`() = runBlockingTest {
        detailViewModel.getLocation(mock(Context::class.java), lat, lon)
        val location = detailViewModel.location.getOrAwaitValue()
        assertNotNull(location)
    }

}