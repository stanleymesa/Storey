package com.example.storey.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storey.data.repository.RetrofitRepository

class RetrofitViewModelFactory private constructor(private val repo: RetrofitRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repo) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(repo) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repo) as T
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> AddStoryViewModel(repo) as T
            else -> throw IllegalArgumentException("Unknown ViewModel Class : ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: RetrofitViewModelFactory? = null

        @JvmStatic
        fun getInstance(repo: RetrofitRepository): RetrofitViewModelFactory {
            if (INSTANCE == null) {
                synchronized(PrefViewModelFactory::class.java) {
                    INSTANCE = RetrofitViewModelFactory(repo)
                }
            }
            return INSTANCE as RetrofitViewModelFactory
        }
    }

}