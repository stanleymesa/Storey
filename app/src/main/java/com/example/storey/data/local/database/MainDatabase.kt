package com.example.storey.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.storey.data.remote.response.ListStoryItem

@Database(entities = [StoryWidget::class, ListStoryItem::class, RemoteKeys::class],
    version = 2,
    exportSchema = false)
abstract class MainDatabase : RoomDatabase() {

    abstract fun storyWidgetDao(): StoryWidgetDao
    abstract fun listStoryitemDao(): ListStoryItemDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): MainDatabase {
            if (INSTANCE == null) {
                synchronized(MainDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MainDatabase::class.java,
                        "main_database"
                    ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
                }
            }
            return INSTANCE as MainDatabase
        }
    }

}