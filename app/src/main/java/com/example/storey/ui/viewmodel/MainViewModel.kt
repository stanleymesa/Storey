package com.example.storey.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storey.data.local.database.MainDatabase
import com.example.storey.data.remote.api.ApiServices
import com.example.storey.data.remote.response.GetAllStoriesResponse
import com.example.storey.data.remote.response.ListStoryItem
import com.example.storey.data.repository.RetrofitRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repo: RetrofitRepository) : ViewModel() {
    private var _storiesWidget = MutableLiveData<GetAllStoriesResponse>()
    val storiesWidget: LiveData<GetAllStoriesResponse> = _storiesWidget

    fun getStories(
        token: String,
        apiServices: ApiServices,
        database: MainDatabase,
    ): LiveData<PagingData<ListStoryItem>> {
        return repo.getAllStories(token, apiServices, database).cachedIn(viewModelScope)
    }

    fun getStoriesWidget(token: String, size: Int) {
        viewModelScope.launch {
            val response = repo.getStoriesWidget(token, size)
            response?.let {
                _storiesWidget.postValue(it)
            }
        }
    }


}