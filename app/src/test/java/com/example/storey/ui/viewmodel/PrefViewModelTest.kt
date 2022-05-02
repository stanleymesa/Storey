package com.example.storey.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storey.data.local.datastore.UserPreferences
import com.example.storey.data.local.model.UserModel
import com.example.storey.getOrAwaitValue
import com.example.storey.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
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
class PrefViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var userPref: UserPreferences
    private lateinit var prefViewModel: PrefViewModel
    private val dummyUserModel = UserModel("123456", true)

    @Before
    fun before() {
        prefViewModel = PrefViewModel(userPref)
    }

    @Test
    fun `when get session should not null`() =
        mainCoroutineRule.runBlockingTest {
            val flow: Flow<UserModel> = flow {
                emit(UserModel(token = "123456", isLogin = true))
            }

            `when`(userPref.getSession()).thenReturn(flow)
            val actual = prefViewModel.getSession().getOrAwaitValue()
            assertNotNull(actual)
        }

    @Test
    fun `when login should call login from userPref`() =
        mainCoroutineRule.runBlockingTest {
            prefViewModel.login(dummyUserModel)
            Mockito.verify(userPref).login(dummyUserModel)
        }

    @Test
    fun `when logout should call logout from userPref`() =
        mainCoroutineRule.runBlockingTest {
            prefViewModel.logout()
            Mockito.verify(userPref).logout()
        }

}