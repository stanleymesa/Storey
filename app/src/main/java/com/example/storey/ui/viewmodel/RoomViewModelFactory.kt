package com.example.storey.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storey.data.local.database.MainDatabase
import com.example.storey.data.local.database.StoryWidgetDao

class RoomViewModelFactory private constructor(private val database: MainDatabase, private val storyWidgetDao: StoryWidgetDao) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryWidgetViewModel::class.java)) {
            return StoryWidgetViewModel(database, storyWidgetDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class : ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: RoomViewModelFactory? = null

        @JvmStatic
        fun getInstance(database: MainDatabase, storyWidgetDao: StoryWidgetDao): RoomViewModelFactory {
            if (INSTANCE == null) {
                synchronized(RoomViewModelFactory::class.java) {
                    INSTANCE = RoomViewModelFactory(database, storyWidgetDao)
                }
            }
            return INSTANCE as RoomViewModelFactory
        }
    }

}