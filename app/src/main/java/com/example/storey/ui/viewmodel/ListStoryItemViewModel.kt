package com.example.storey.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storey.data.local.database.ListStoryItemDao
import com.example.storey.data.local.database.MainDatabase
import com.example.storey.data.remote.response.ListStoryItem
import com.example.storey.data.repository.ListStoryItemRepository

class ListStoryItemViewModel(database: MainDatabase, listStoryItemDao: ListStoryItemDao): ViewModel() {
    private val repo = ListStoryItemRepository(database, listStoryItemDao)

    fun getAllListStoryItem(): LiveData<List<ListStoryItem>> = repo.getAllListStoryItem()
}