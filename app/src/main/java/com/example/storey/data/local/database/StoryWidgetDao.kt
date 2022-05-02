package com.example.storey.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryWidgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(storyWidget: StoryWidget)

    @Query("DELETE FROM storywidget")
    suspend fun delete()

    @Query("SELECT * FROM storywidget ORDER BY id ASC")
    fun getAllStoryWidget(): List<StoryWidget>
}