package com.example.eduapp.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * One completed quiz round. The statistics screen is built entirely from
 * this table, so nothing needs to be stored twice.
 */
@Entity(
    tableName = "quiz_results",
    indices = [Index("username"), Index("level")]
)
data class QuizResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val level: Int,
    val correct: Int,
    val total: Int,
    val durationSeconds: Int,
    val playedAt: Long = System.currentTimeMillis()
)
