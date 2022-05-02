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
class LoginViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repo: RetrofitRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginModel = DummyData.getDummyLoginModel()
    private val dummyResponse = DummyData.getDummyLoginResponseSuccess()
    private val dummyResponseFailed = DummyData.getDummyLoginResponseFailed()

    @Before
    fun before() {
        loginViewModel = LoginViewModel(repo)
    }

    @Test
    fun `when login should call login on repository`() = mainCoroutineRule.runBlockingTest {
        loginViewModel.login(dummyLoginModel)
        Mockito.verify(repo).login(dummyLoginModel)
    }

    @Test
    fun `when login success should get token and not null`() = mainCoroutineRule.runBlockingTest {
        val expectedResponse = dummyResponse
        `when`(repo.login(dummyLoginModel)).thenReturn(expectedResponse)
        loginViewModel.login(dummyLoginModel)

        val actualResponse = loginViewModel.loginResponse.getOrAwaitValue()
        assertNotNull(actualResponse)
        assertEquals(expectedResponse.loginResult.token, actualResponse.loginResult.token)
    }

    @Test
    fun `when login failed should get response and not null`() = mainCoroutineRule.runBlockingTest {
        val expectedResponse = dummyResponseFailed
        `when`(repo.login(dummyLoginModel)).thenReturn(expectedResponse)
        loginViewModel.login(dummyLoginModel)

        val actualResponse = loginViewModel.loginResponse.getOrAwaitValue()
        assertNotNull(actualResponse)
        assertEquals(true, actualResponse.error)
    }
}