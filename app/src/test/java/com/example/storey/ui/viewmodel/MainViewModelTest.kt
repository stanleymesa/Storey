package com.example.storey.ui.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.room.Room
import com.example.storey.DummyData
import com.example.storey.data.FakeApiService
import com.example.storey.data.local.database.MainDatabase
import com.example.storey.data.remote.api.ApiServices
import com.example.storey.data.remote.response.ListStoryItem
import com.example.storey.data.repository.RetrofitRepository
import com.example.storey.getOrAwaitValue
import com.example.storey.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repo: RetrofitRepository
    private lateinit var mainViewModel: MainViewModel
    private lateinit var apiServices: ApiServices
    private lateinit var database: MainDatabase
    private val dummyResponse = DummyData.getDummyStoryResponseSuccess()
    private val dummyResponseFailed = DummyData.getDummyStoryResponseFailed()
    private val dummyToken = "qwdbj34Bon"
    private val dummySize = 5

    @Before
    fun before() {
        mainViewModel = MainViewModel(repo)
        apiServices = FakeApiService()
        database = Room.inMemoryDatabaseBuilder(
            Mockito.mock(Context::class.java),
            MainDatabase::class.java
        ).build()
    }

    @Test
    fun `when get stories should call getAllStories from repository`() =
        mainCoroutineRule.runBlockingTest {
            val liveData = MutableLiveData<PagingData<ListStoryItem>>()
            liveData.value = PagingData.empty()

            `when`(repo.getAllStories(dummyToken, apiServices, database)).thenReturn(liveData)
            mainViewModel.getStories(dummyToken, apiServices, database)

            verify(repo).getAllStories(dummyToken, apiServices, database)
        }

    @Test
    fun `when get stories widget should not null`() =
        mainCoroutineRule.runBlockingTest {
            mainViewModel.getStoriesWidget(dummyToken, dummySize)
            verify(repo).getStoriesWidget(dummyToken, dummySize)
        }

    @Test
    fun `when get story for widget should call getStoriesWidget from repository`() =
        mainCoroutineRule.runBlockingTest {
            mainViewModel.getStoriesWidget(dummyToken, dummySize)
            verify(repo).getStoriesWidget(dummyToken, dummySize)
        }

    @Test
    fun `when get story success should get photo url and not null`() =
        mainCoroutineRule.runBlockingTest {
            `when`(repo.getStoriesWidget(dummyToken, dummySize)).thenReturn(dummyResponse)
            mainViewModel.getStoriesWidget(dummyToken, dummySize)

            val actualResponse = mainViewModel.storiesWidget.getOrAwaitValue()
            assertNotNull(actualResponse)
            assertEquals(dummyResponse.listStory[0].photoUrl, actualResponse.listStory[0].photoUrl)
        }

    @Test
    fun `when get story failed should get response and not null`() =
        mainCoroutineRule.runBlockingTest {
            `when`(repo.getStoriesWidget(dummyToken, dummySize))
                .thenReturn(dummyResponseFailed)
            mainViewModel.getStoriesWidget(dummyToken, dummySize)

            val actualResponse = mainViewModel.storiesWidget.getOrAwaitValue()
            assertNotNull(actualResponse)
            assertEquals(true, actualResponse.error)
        }
}