package com.example.storey.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storey.DummyData
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
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repo: RetrofitRepository
    private lateinit var addStoryViewModel: AddStoryViewModel
    private val dummyUploadModel = DummyData.getDummyUploadModel()
    private val dummyUploadResponse = DummyData.getDummyUploadResponseSuccess()
    private val dummyUploadResponseFailed = DummyData.getDummyUploadResponseFailed()

    @Before
    fun before() {
        addStoryViewModel = AddStoryViewModel(repo)
    }

    @Test
    fun `when upload story should call uploadStory from repository`() =
        mainCoroutineRule.runBlockingTest {
            addStoryViewModel.uploadStory(dummyUploadModel)
            Mockito.verify(repo).uploadStory(dummyUploadModel)
        }

    @Test
    fun `when upload success should get response and not null`() =
        mainCoroutineRule.runBlockingTest {
            `when`(repo.uploadStory(dummyUploadModel)).thenReturn(dummyUploadResponse)
            addStoryViewModel.uploadStory(dummyUploadModel)

            val actualResponse = addStoryViewModel.fileUploadResponse.getOrAwaitValue()
            assertNotNull(actualResponse)
            assertEquals(dummyUploadResponse, actualResponse)
        }

    @Test
    fun `when upload failed should get response and not null`() =
        mainCoroutineRule.runBlockingTest {
            `when`(repo.uploadStory(dummyUploadModel)).thenReturn(dummyUploadResponseFailed)
            addStoryViewModel.uploadStory(dummyUploadModel)

            val actualResponse = addStoryViewModel.fileUploadResponse.getOrAwaitValue()
            assertNotNull(actualResponse)
            assertEquals(true, actualResponse.error)
        }
}