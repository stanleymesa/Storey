package com.example.storey.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storey.data.local.model.LoginModel
import com.example.storey.data.remote.response.LoginResponse
import com.example.storey.data.repository.RetrofitRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: RetrofitRepository) : ViewModel() {

    private var _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(loginModel: LoginModel) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val response = repo.login(loginModel)

            response?.let {
                _loginResponse.postValue(it)
            }
            _isLoading.postValue(false)
        }
    }

}