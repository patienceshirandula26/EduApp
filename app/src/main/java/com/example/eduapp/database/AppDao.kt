package com.example.eduapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Insert
    suspend fun insertResult(result: QuizResult)

    @Query("SELECT * FROM quiz_results WHERE username = :username ORDER BY playedAt DESC")
    fun resultsFor(username: String): Flow<List<QuizResult>>

    @Query("SELECT * FROM quiz_results ORDER BY playedAt DESC")
    fun allResults(): Flow<List<QuizResult>>

    @Query("SELECT COUNT(*) FROM quiz_results WHERE username = :username")
    fun quizzesPlayed(username: String): Flow<Int>

    @Query("SELECT COALESCE(SUM(correct), 0) FROM quiz_results WHERE username = :username")
    fun totalCorrect(username: String): Flow<Int>

    @Query("SELECT COALESCE(MAX(correct), 0) FROM quiz_results WHERE username = :username AND level = :level")
    fun bestForLevel(username: String, level: Int): Flow<Int>

    @Query("DELETE FROM quiz_results WHERE username = :username")
    suspend fun clearFor(username: String)

    @Query("DELETE FROM quiz_results")
    suspend fun clearAll()
}
