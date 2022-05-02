package com.example.storey.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storey.data.local.database.ListStoryItemDao
import com.example.storey.data.local.database.MainDatabase

class ListStoryItemViewModelFactory private constructor(private val database: MainDatabase, private val listStoryItemDao: ListStoryItemDao) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ListStoryItemViewModel::class.java) -> ListStoryItemViewModel(
                database, listStoryItemDao) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class : ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ListStoryItemViewModelFactory? = null

        @JvmStatic
        fun getInstance(database: MainDatabase, listStoryItemDao: ListStoryItemDao): ListStoryItemViewModelFactory {
            if (INSTANCE == null) {
                synchronized(PrefViewModelFactory::class.java) {
                    INSTANCE = ListStoryItemViewModelFactory(database, listStoryItemDao)
                }
            }
            return INSTANCE as ListStoryItemViewModelFactory
        }
    }

}