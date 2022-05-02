package com.example.storey.data.repository

import androidx.lifecycle.LiveData
import com.example.storey.data.local.database.ListStoryItemDao
import com.example.storey.data.local.database.MainDatabase
import com.example.storey.data.remote.response.ListStoryItem

class ListStoryItemRepository(database: MainDatabase, listStoryItemDao: ListStoryItemDao) {
    private val mListStoryItemDao: ListStoryItemDao

    init {
        val db = database
        mListStoryItemDao = listStoryItemDao
    }

    fun getAllListStoryItem(): LiveData<List<ListStoryItem>> =
        mListStoryItemDao.getAllListStoryForMaps()
}