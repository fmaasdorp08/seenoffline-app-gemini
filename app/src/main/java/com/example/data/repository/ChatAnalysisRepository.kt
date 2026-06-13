package com.example.data.repository

import com.example.data.db.ChatAnalysisDao
import com.example.data.model.ChatAnalysis
import kotlinx.coroutines.flow.Flow

class ChatAnalysisRepository(private val chatAnalysisDao: ChatAnalysisDao) {
    val allAnalyses: Flow<List<ChatAnalysis>> = chatAnalysisDao.getAllAnalyses()

    suspend fun getAnalysisById(id: Long): ChatAnalysis? {
        return chatAnalysisDao.getAnalysisById(id)
    }

    suspend fun insertAnalysis(analysis: ChatAnalysis): Long {
        return chatAnalysisDao.insertAnalysis(analysis)
    }

    suspend fun deleteAnalysis(analysis: ChatAnalysis) {
        chatAnalysisDao.deleteAnalysis(analysis)
    }

    suspend fun clearAllAnalyses() {
        chatAnalysisDao.clearAllAnalyses()
    }
}
