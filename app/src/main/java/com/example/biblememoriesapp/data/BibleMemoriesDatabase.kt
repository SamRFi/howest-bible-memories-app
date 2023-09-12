package com.example.biblememoriesapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Memory::class], version = 1, exportSchema = false)
abstract class BibleMemoriesDatabase : RoomDatabase() {

    abstract fun memoryDao(): MemoryDao

    companion object {
        @Volatile
        private var INSTANCE: BibleMemoriesDatabase? = null

        fun getInstance(context: Context): BibleMemoriesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BibleMemoriesDatabase::class.java,
                    "bible_memories_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
