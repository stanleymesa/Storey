package com.example.storey.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storey.data.local.datastore.UserPreferences
import com.example.storey.data.local.model.UserModel
import kotlinx.coroutines.launch

class PrefViewModel(private val pref: UserPreferences) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return pref.getSession().asLiveData()
    }

    fun login(userModel: UserModel) {
        viewModelScope.launch {
            pref.login(userModel)
        }
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

}