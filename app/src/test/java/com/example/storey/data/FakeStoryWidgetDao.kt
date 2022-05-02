package com.example.storey.data

import com.example.storey.data.local.database.StoryWidget
import com.example.storey.data.local.database.StoryWidgetDao

class FakeStoryWidgetDao: StoryWidgetDao {

    private var listData = arrayListOf<StoryWidget>()

    override suspend fun insert(storyWidget: StoryWidget) {
        listData.add(storyWidget)
    }

    override suspend fun delete() {
        listData.clear()
    }

    override fun getAllStoryWidget(): List<StoryWidget> {
        return listData
    }
}