package com.example.storey.data.local.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storey.data.remote.response.ListStoryItem

@Dao
interface ListStoryItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertListStory(listStory: List<ListStoryItem>)

    @Query("SELECT * FROM list_story")
    fun getAllListStory(): PagingSource<Int, ListStoryItem>

    @Query("SELECT * FROM list_story")
    fun getAllListStoryForMaps(): LiveData<List<ListStoryItem>>

    @Query("DELETE FROM list_story")
    suspend fun deleteAll()

}