package com.example.storey.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storey.data.local.database.MainDatabase
import com.example.storey.data.local.database.StoryWidget
import com.example.storey.data.local.database.StoryWidgetDao
import com.example.storey.data.repository.StoryWidgetRepository
import kotlinx.coroutines.launch

class StoryWidgetViewModel(database: MainDatabase, storyWidgetDao: StoryWidgetDao) : ViewModel() {
    private val mStoryWidgetRepository = StoryWidgetRepository(database, storyWidgetDao)

    fun insert(storyWidget: StoryWidget) {
        viewModelScope.launch {
            mStoryWidgetRepository.insert(storyWidget)
        }
    }

    fun delete() {
        viewModelScope.launch {
            mStoryWidgetRepository.delete()
        }
    }

    fun getStoryWidget(): List<StoryWidget> {
        return mStoryWidgetRepository.getStoryWidget()
    }
}