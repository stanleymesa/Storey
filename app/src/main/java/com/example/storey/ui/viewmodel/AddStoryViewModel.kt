package com.example.storey.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storey.data.local.model.UploadModel
import com.example.storey.data.remote.response.FileUploadResponse
import com.example.storey.data.repository.RetrofitRepository
import kotlinx.coroutines.launch

class AddStoryViewModel(private val repo: RetrofitRepository) : ViewModel() {
    private var _fileUploadResponse = MutableLiveData<FileUploadResponse>()
    val fileUploadResponse: LiveData<FileUploadResponse> = _fileUploadResponse

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadStory(uploadModel: UploadModel) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val response = repo.uploadStory(uploadModel)
            response?.let {
                _fileUploadResponse.postValue(it)
            }
            _isLoading.postValue(false)
        }
    }
}