package com.example.storey.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storey.data.local.model.RegisterModel
import com.example.storey.data.remote.response.RegisterResponse
import com.example.storey.data.repository.RetrofitRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repo: RetrofitRepository) : ViewModel() {

    private var _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(registerModel: RegisterModel) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val response = repo.register(registerModel)
            response?.let {
                _registerResponse.postValue(it)
            }
            _isLoading.postValue(false)
        }
    }

}