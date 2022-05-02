package com.example.storey.data.repository

import com.example.storey.data.local.database.MainDatabase
import com.example.storey.data.local.database.StoryWidget
import com.example.storey.data.local.database.StoryWidgetDao

class StoryWidgetRepository(database: MainDatabase, storyWidgetDao: StoryWidgetDao) {
    private val mStoryWidgetDao: StoryWidgetDao

    init {
        val db = database
        mStoryWidgetDao = storyWidgetDao
    }

    suspend fun insert(storyWidget: StoryWidget) = mStoryWidgetDao.insert(storyWidget)

    suspend fun delete() = mStoryWidgetDao.delete()

    fun getStoryWidget(): List<StoryWidget> {
        return mStoryWidgetDao.getAllStoryWidget()
    }

}