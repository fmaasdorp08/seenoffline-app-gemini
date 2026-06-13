package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "chat_analyses")
@JsonClass(generateAdapter = true)
data class ChatAnalysis(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val rawText: String,
    val timestamp: Long = System.currentTimeMillis(),
    
    // Core Metrics (0 - 100)
    val interestScore: Int,
    val ghostingProbability: Int,
    val emotionalBalanceIndex: Int, // -100 to 100. Lower = user overinvested, Higher = counterparty overinvested, 0 = balanced
    val attractionMeter: Int,
    val trustScore: Int,
    val authenticityScore: Int,
    
    // Categorical Analysis
    val intentLikelihood: String, // Casual, Committed, Fading, Manipulative
    val powerDynamicRating: String, // User Dominated, Counterparty Dominated, Balanced
    val manipulationPattern: String, // Gaslighting, Lovebombing, Authentic, Passive Aggressive
    val attachmentStyle: String, // Secure, anxious, avoidant, disorganized
    
    // Qualitative Text
    val brutalTruth: String, // Roasting summary
    val nextMove: String, // Recommended next reply or action
    
    // Serialized Lists (Hidden meanings, key dynamic takeaways)
    val serializedHiddenMeanings: String, // JSON list of HiddenMeaning key-values
    val serializedTakeaways: String // JSON list of string takeaways
)

data class HiddenMeaning(
    val originalPhrase: String,
    val translation: String,
    val severity: String // "LOW", "MEDIUM", "DANGER"
)
