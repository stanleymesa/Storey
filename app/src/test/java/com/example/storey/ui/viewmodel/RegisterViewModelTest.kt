package com.example.storey.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storey.DummyData
import com.example.storey.data.remote.response.RegisterResponse
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
class RegisterViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repo: RetrofitRepository
    private lateinit var registerViewModel: RegisterViewModel
    private val dummyAccount = DummyData.getDummyRegisterModel()

    @Before
    fun before() {
        registerViewModel = RegisterViewModel(repo)
    }

    @Test
    fun `when register account should call register from repository`() =
        mainCoroutineRule.runBlockingTest {
            registerViewModel.register(dummyAccount)
            Mockito.verify(repo).register(dummyAccount)
        }

    @Test
    fun `when register success should get response and not null`() =
        mainCoroutineRule.runBlockingTest {
            val expectedResponse = RegisterResponse(error = false, message = "Success")
            `when`(repo.register(dummyAccount)).thenReturn(expectedResponse)
            registerViewModel.register(dummyAccount)

            val actualResponse = registerViewModel.registerResponse.getOrAwaitValue()
            assertNotNull(actualResponse)
            assertEquals(expectedResponse, actualResponse)
        }

    @Test
    fun `when register failed should get response and not null`() =
        mainCoroutineRule.runBlockingTest {
            val expectedResponse = RegisterResponse(error = true, message = "Email Not Valid")
            `when`(repo.register(dummyAccount)).thenReturn(expectedResponse)
            registerViewModel.register(dummyAccount)

            val actualResponse = registerViewModel.registerResponse.getOrAwaitValue()
            assertNotNull(actualResponse)
            assertEquals(true, actualResponse.error)
        }
}