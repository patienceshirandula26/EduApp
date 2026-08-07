package com.example.eduapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [QuizResult::class], version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}
