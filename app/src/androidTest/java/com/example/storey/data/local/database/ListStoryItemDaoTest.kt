package com.example.storey.data.local.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.storey.DummyData
import com.example.storey.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ListStoryItemDaoTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MainDatabase
    private lateinit var dao: ListStoryItemDao
    private val dummyListStoryItem = DummyData.getDummyStoryItems()

    @Before
    fun before() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MainDatabase::class.java
        ).build()
        dao = database.listStoryitemDao()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertAndGetStory() = runBlockingTest {
        dao.insertListStory(dummyListStoryItem)
        val actual = dao.getAllListStoryForMaps().getOrAwaitValue()
        assertNotNull(actual)
        assertEquals(dummyListStoryItem[0].id, actual[0].id)
    }

    @Test
    fun deleteAllStory() = runBlockingTest {
        dao.insertListStory(dummyListStoryItem)
        dao.deleteAll()
        val actual = dao.getAllListStoryForMaps().getOrAwaitValue()
        assertTrue(actual.isEmpty())
    }

}