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
class RemoteKeysDaoTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: MainDatabase
    private lateinit var dao: RemoteKeysDao
    private val dummylistRemoteKeys = DummyData.getDummyRemoteKeys()

    @Before
    fun before() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MainDatabase::class.java
        ).build()
        dao = database.remoteKeysDao()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertAndGetStory() = runBlockingTest {
        dao.insertAll(dummylistRemoteKeys)
        val actual = dao.getRemoteKeys(dummylistRemoteKeys[0].id)
        assertNotNull(actual)
        assertEquals(dummylistRemoteKeys[0].id, actual!!.id)
    }

    @Test
    fun deleteAllStory() = runBlockingTest {
        dao.insertAll(dummylistRemoteKeys)
        dao.deleteRemoteKeys()
        val actual = dao.getRemoteKeys(dummylistRemoteKeys[0].id)
        assertNull(actual)
    }
}