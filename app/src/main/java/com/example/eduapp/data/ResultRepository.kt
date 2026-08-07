package com.example.eduapp.data

import com.example.eduapp.database.AppDao
import com.example.eduapp.database.User
import kotlinx.coroutines.flow.Flow

/**
 * Wraps the Room DAO so the ViewModel never touches the database directly.
 */
interface ResultRepository {
    val results: Flow<List<User>>
    suspend fun saveResult(result: User)
    suspend fun clearResults()
}

class ResultRepositoryImpl(private val dao: AppDao) : ResultRepository {

    override val results: Flow<List<User>> = dao.getAllUsers()

    override suspend fun saveResult(result: User) = dao.insert(result)

    override suspend fun clearResults() = dao.deleteAll()
}