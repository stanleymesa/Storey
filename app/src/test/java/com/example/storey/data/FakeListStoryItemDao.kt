package com.example.storey.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import com.example.storey.data.local.database.ListStoryItemDao
import com.example.storey.data.remote.response.ListStoryItem

class FakeListStoryItemDao: ListStoryItemDao {
    private var listData = arrayListOf<ListStoryItem>()

    override suspend fun insertListStory(listStory: List<ListStoryItem>) {
        TODO("Not yet implemented")
    }

    override fun getAllListStory(): PagingSource<Int, ListStoryItem> {
        TODO("Not yet implemented")
    }

    override fun getAllListStoryForMaps(): LiveData<List<ListStoryItem>> {
        val liveData = MutableLiveData<List<ListStoryItem>>()
        liveData.value = listData
        return liveData
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }
}