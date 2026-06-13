package com.example.data.db

import androidx.room.*
import com.example.data.model.ChatAnalysis
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatAnalysisDao {
    @Query("SELECT * FROM chat_analyses ORDER BY timestamp DESC")
    fun getAllAnalyses(): Flow<List<ChatAnalysis>>

    @Query("SELECT * FROM chat_analyses WHERE id = :id LIMIT 1")
    suspend fun getAnalysisById(id: Long): ChatAnalysis?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnalysis(analysis: ChatAnalysis): Long

    @Delete
    suspend fun deleteAnalysis(analysis: ChatAnalysis)

    @Query("DELETE FROM chat_analyses")
    suspend fun clearAllAnalyses()
}
