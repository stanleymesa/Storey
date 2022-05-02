package com.example.storey.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.storey.data.local.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map {
            UserModel(
                token = it[KEY_TOKEN] ?: "",
                isLogin = it[KEY_ISLOGIN] ?: false
            )
        }
    }

    suspend fun login(userModel: UserModel) {
        dataStore.edit {
            it[KEY_TOKEN] = userModel.token
            it[KEY_ISLOGIN] = true
        }
    }

    suspend fun logout() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val KEY_TOKEN = stringPreferencesKey("token")
        private val KEY_ISLOGIN = booleanPreferencesKey("islogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }

    }

}