package com.example.storey.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import com.example.storey.data.FakeListStoryItemDao
import com.example.storey.data.local.database.MainDatabase
import com.example.storey.getOrAwaitValue
import com.example.storey.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListStoryItemRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var context: Context
    private lateinit var listStoryItemRepository: ListStoryItemRepository
    private lateinit var db: MainDatabase
    private lateinit var dao: FakeListStoryItemDao

    @Before
    fun before() {
        db = Room.inMemoryDatabaseBuilder(
            context,
            MainDatabase::class.java
        ).build()
        dao = FakeListStoryItemDao()
        listStoryItemRepository = ListStoryItemRepository(db, dao)
    }

    @Test
    fun `when get story should not null`() = mainCoroutineRule.runBlockingTest {
        val actual = listStoryItemRepository.getAllListStoryItem().getOrAwaitValue()
        assertNotNull(actual)
    }

}