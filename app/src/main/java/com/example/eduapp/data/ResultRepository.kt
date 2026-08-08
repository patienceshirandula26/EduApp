package com.example.eduapp.data

import com.example.eduapp.database.AppDao
import com.example.eduapp.database.QuizResult
import kotlinx.coroutines.flow.Flow

interface ResultRepository {
    fun resultsFor(username: String): Flow<List<QuizResult>>
    fun quizzesPlayed(username: String): Flow<Int>
    fun totalCorrect(username: String): Flow<Int>
    fun totalAttempted(username: String): Flow<Int>
    fun bestScore(username: String): Flow<Int>
    fun bestForLevel(username: String, level: Int): Flow<Int>
    fun averageDuration(username: String): Flow<Double>
    fun fastestRound(username: String): Flow<Int>
    suspend fun saveResult(result: QuizResult)
    suspend fun clearFor(username: String)
}

class ResultRepositoryImpl(private val dao: AppDao) : ResultRepository {

    override fun resultsFor(username: String): Flow<List<QuizResult>> = dao.resultsFor(username)

    override fun quizzesPlayed(username: String): Flow<Int> = dao.quizzesPlayed(username)

    override fun totalCorrect(username: String): Flow<Int> = dao.totalCorrect(username)

    override fun totalAttempted(username: String): Flow<Int> = dao.totalAttempted(username)

    override fun bestScore(username: String): Flow<Int> = dao.bestScore(username)

    override fun averageDuration(username: String): Flow<Double> = dao.averageDuration(username)

    override fun fastestRound(username: String): Flow<Int> = dao.fastestRound(username)

    override fun bestForLevel(username: String, level: Int): Flow<Int> =
        dao.bestForLevel(username, level)

    override suspend fun saveResult(result: QuizResult) = dao.insertResult(result)

    override suspend fun clearFor(username: String) = dao.clearFor(username)
}
