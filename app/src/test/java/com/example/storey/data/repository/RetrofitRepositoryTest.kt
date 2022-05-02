package com.example.storey.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import com.example.storey.DummyData
import com.example.storey.data.FakeApiService
import com.example.storey.data.local.database.MainDatabase
import com.example.storey.data.remote.api.ApiServices
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
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RetrofitRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var apiServices: ApiServices
    private lateinit var repo: RetrofitRepository
    private lateinit var database: MainDatabase

    @Before
    fun before() {
        apiServices = FakeApiService()
        repo = RetrofitRepository()
        repo.apiServices = apiServices
        database = Room.inMemoryDatabaseBuilder(
            mock(Context::class.java),
            MainDatabase::class.java
        ).build()
    }

    @Test
    fun `when register should get response and not null`() = mainCoroutineRule.runBlockingTest {
        val expectedResponse = DummyData.getDummyRegisterResponse()
        val actualResponse = repo.register(DummyData.getDummyRegisterModel())
        assertNotNull(actualResponse)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `when login should get response and not null`() = mainCoroutineRule.runBlockingTest {
        val expectedResponse = DummyData.getDummyLoginResponseSuccess()
        val actualResponse = repo.login(DummyData.getDummyLoginModel())
        assertNotNull(actualResponse)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `when get stories should get response and not null`() = mainCoroutineRule.runBlockingTest {
        val actualResponse = repo.getAllStories(
            token = "asdkhabskdAD",
            apiServices,
            database
        ).getOrAwaitValue()

        assertNotNull(actualResponse)
    }

    @Test
    fun `when get stories widget should get response and not null`() = mainCoroutineRule.runBlockingTest {
        val actualResponse = repo.getStoriesWidget(
            token = "asdkhabskdAD",
            size = 5
        )

        assertNotNull(actualResponse)
    }

    @Test
    fun `when upload story should get response and not null`() = mainCoroutineRule.runBlockingTest {
        val expectedResponse = DummyData.getDummyUploadResponseSuccess()
        val actualResponse = repo.uploadStory(
            DummyData.getDummyUploadModel()
        )
        assertNotNull(actualResponse)
        assertEquals(expectedResponse, actualResponse)
    }
}