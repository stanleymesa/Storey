package com.example.storey.data.local.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.storey.DummyData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StoryWidgetDaoTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MainDatabase
    private lateinit var dao: StoryWidgetDao
    private val dummyStoryWidget = DummyData.getDummyStoryWidget()

    @Before
    fun before() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MainDatabase::class.java
        ).build()
        dao = database.storyWidgetDao()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertAndGetStoryWidget() = runBlockingTest {
        dao.insert(dummyStoryWidget)
        val actual = dao.getAllStoryWidget()
        assertNotNull(actual)
        assertEquals(dummyStoryWidget, actual[0])
    }

    @Test
    fun deleteAllStoryWidget() = runBlockingTest {
        dao.insert(dummyStoryWidget)
        dao.delete()
        val actual = dao.getAllStoryWidget()
        assertTrue(actual.isEmpty())
    }
}