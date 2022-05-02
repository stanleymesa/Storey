package com.example.storey.ui.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import com.example.storey.DummyData
import com.example.storey.data.FakeStoryWidgetDao
import com.example.storey.data.local.database.MainDatabase
import com.example.storey.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryWidgetViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var context: Context
    private lateinit var storyWidgetViewModel: StoryWidgetViewModel
    private lateinit var db: MainDatabase
    private lateinit var dao: FakeStoryWidgetDao

    @Before
    fun before() {
        db = Room.inMemoryDatabaseBuilder(
            context,
            MainDatabase::class.java
        ).build()
        dao = FakeStoryWidgetDao()
        storyWidgetViewModel = StoryWidgetViewModel(db, dao)
    }

    @Test
    fun `when insert should not null and return data`() = runBlockingTest {
        storyWidgetViewModel.insert(DummyData.getDummyStoryWidget())
        val actual = storyWidgetViewModel.getStoryWidget()
        assertNotNull(actual)
        assertEquals(DummyData.getDummyStoryWidget(), actual[0])
    }

    @Test
    fun `when delete should empty data`() = runBlockingTest {
        storyWidgetViewModel.delete()
        val actual = storyWidgetViewModel.getStoryWidget()
        assertTrue(actual.isEmpty())
    }

    @Test
    fun `when get story should not null`() = runBlockingTest {
        val actual = storyWidgetViewModel.getStoryWidget()
        assertNotNull(actual)
    }

}