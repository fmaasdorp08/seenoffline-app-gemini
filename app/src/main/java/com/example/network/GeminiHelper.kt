package com.example.network

import com.example.data.model.ChatAnalysis
import com.example.data.model.HiddenMeaning
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONArray
import org.json.JSONObject

object GeminiHelper {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    /**
     * Build the request payload for Gemini API.
     */
    fun buildRequestBodyJson(prompt: String): String {
        val systemInstruction = """
            You are SeenOffline AI — a brutally brilliant social intelligence compiler, startup strategist, and cultural psychologist.
            Your task is to analyze a conversation snippet (chats, screenshots, DMs, WhatsApp, iMessage, Tinder/Bumble, LinkedIn, etc.) and render a highly advanced psychological, conversational, and energetic assessment.
            You must be extremely intelligent, witty, slightly dangerous (but entirely safe), highly shareable, and spot-on. Frame it like a Gossip Girl report, a Spotify Wrapped breakdown, or a premium A24 cultural artifact.
            
            Analyze the relationship dynamics across the following criteria. Always return response exactly in JSON format containing these fields SPECIFICALLY:
            - interestScore: Integer from 0 to 100
            - ghostingProbability: Integer from 0 to 100
            - emotionalBalanceIndex: Integer from -100 to 100 (Negative means user over-invested, positive means counter-party over-invested, 0 is balanced)
            - attractionMeter: Integer from 0 to 100
            - trustScore: Integer from 0 to 100
            - authenticityScore: Integer from 0 to 100
            - intentLikelihood: String. One of: "Casual Adventure", "Committed Pursuit", "Quiet Fading", "Manipulative Game", "Undetermined"
            - powerDynamicRating: String. One of: "Balanced", "Counterparty Dominated", "User Dominated"
            - manipulationPattern: String. e.g. "Gaslighting", "Love bombing", "Breadcrumbing", "None (Authentic)", "Passive Aggressive"
            - attachmentStyle: String. One of: "Secure Connection", "Anxious Spiral", "Avoidant Distance", "Anxious-Avoidant Imbalance"
            - brutalTruth: String. A sharp, smart, witty explanation of what is REALLY going on (maximum 3 concise sentences). Highly engaging, slightly roasting.
            - nextMove: String. Tactical action advice or response option.
            - hiddenMeanings: A list of objects. Each object has:
                * originalPhrase: String (specific quote from the chat)
                * translation: String (what they actually meant)
                * severity: String. One of: "LOW", "MEDIUM", "DANGER"
            - takeaways: A list of string bullet points (max 3).
            
            Do not include any greeting or conversational filler. Output ONLY a single valid JSON object.
        """.trimIndent()

        // Build request body using native JSON library to avoid serialization overhead
        val request = JSONObject()
        val contents = JSONArray()
        val content = JSONObject()
        val parts = JSONArray()
        val textPart = JSONObject()
        textPart.put("text", prompt)
        parts.put(textPart)
        content.put("parts", parts)
        contents.put(content)
        request.put("contents", contents)

        val generationConfig = JSONObject()
        generationConfig.put("responseMimeType", "application/json")
        generationConfig.put("temperature", 0.7)
        request.put("generationConfig", generationConfig)

        val systemContent = JSONObject()
        val systemParts = JSONArray()
        val systemTextPart = JSONObject()
        systemTextPart.put("text", systemInstruction)
        systemParts.put(systemTextPart)
        systemContent.put("parts", systemParts)
        request.put("systemInstruction", systemContent)

        return request.toString()
    }

    /**
     * Parses the response from Gemini.
     */
    fun parseGeminiResponse(rawResponseBody: String, title: String, rawText: String): ChatAnalysis {
        // Clean markdown backticks if present (e.g., ```json { ... } ```)
        var cleaned = rawResponseBody.trim()
        if (cleaned.startsWith("```")) {
            val lines = cleaned.split("\n")
            val filteredLines = lines.filter { !it.trim().startsWith("```") }
            cleaned = filteredLines.joinToString("\n").trim()
        }

        val json = JSONObject(cleaned)

        val interestScore = json.optInt("interestScore", 50)
        val ghostingProbability = json.optInt("ghostingProbability", 10)
        val emotionalBalanceIndex = json.optInt("emotionalBalanceIndex", 0)
        val attractionMeter = json.optInt("attractionMeter", 50)
        val trustScore = json.optInt("trustScore", 50)
        val authenticityScore = json.optInt("authenticityScore", 50)

        val intentLikelihood = json.optString("intentLikelihood", "Undetermined")
        val powerDynamicRating = json.optString("powerDynamicRating", "Balanced")
        val manipulationPattern = json.optString("manipulationPattern", "None (Authentic)")
        val attachmentStyle = json.optString("attachmentStyle", "Secure Connection")

        val brutalTruth = json.optString("brutalTruth", "The interaction displays a standard communication pace with no immediate red flags. Keep tracking for deeper signals.")
        val nextMove = json.optString("nextMove", "Match their energy. Wait 1.5x as long to reply and keep your next message brief.")

        // Hidden Meanings
        val hiddenMeaningsList = mutableListOf<HiddenMeaning>()
        val hiddenMeaningsArray = json.optJSONArray("hiddenMeanings")
        if (hiddenMeaningsArray != null) {
            for (i in 0 until hiddenMeaningsArray.length()) {
                val item = hiddenMeaningsArray.getJSONObject(i)
                hiddenMeaningsList.add(
                    HiddenMeaning(
                        originalPhrase = item.optString("originalPhrase", ""),
                        translation = item.optString("translation", ""),
                        severity = item.optString("severity", "LOW")
                    )
                )
            }
        } else {
            // Default empty state
            hiddenMeaningsList.add(HiddenMeaning("N/A", "Awaiting more conversational volume to extract subtext.", "LOW"))
        }

        // Takeaways
        val takeawaysList = mutableListOf<String>()
        val takeawaysArray = json.optJSONArray("takeaways")
        if (takeawaysArray != null) {
            for (i in 0 until takeawaysArray.length()) {
                takeawaysList.add(takeawaysArray.getString(i))
            }
        } else {
            takeawaysList.add("Maintain emotional independence.")
            takeawaysList.add("Match reply frequencies to balance investment.")
        }

        // Serialize lists as JSON for the model
        val meaningsAdapter = moshi.adapter<List<HiddenMeaning>>(
            Types.newParameterizedType(List::class.java, HiddenMeaning::class.java)
        )
        val takeawaysAdapter = moshi.adapter<List<String>>(
            Types.newParameterizedType(List::class.java, String::class.java)
        )

        val serializedHiddenMeanings = meaningsAdapter.toJson(hiddenMeaningsList)
        val serializedTakeaways = takeawaysAdapter.toJson(takeawaysList)

        return ChatAnalysis(
            title = title,
            rawText = rawText,
            interestScore = interestScore,
            ghostingProbability = ghostingProbability,
            emotionalBalanceIndex = emotionalBalanceIndex,
            attractionMeter = attractionMeter,
            trustScore = trustScore,
            authenticityScore = authenticityScore,
            intentLikelihood = intentLikelihood,
            powerDynamicRating = powerDynamicRating,
            manipulationPattern = manipulationPattern,
            attachmentStyle = attachmentStyle,
            brutalTruth = brutalTruth,
            nextMove = nextMove,
            serializedHiddenMeanings = serializedHiddenMeanings,
            serializedTakeaways = serializedTakeaways
        )
    }

    /**
     * Parse serialized hidden meanings from DB back to logic objects.
     */
    fun getHiddenMeanings(analysis: ChatAnalysis): List<HiddenMeaning> {
        return try {
            val adapter = moshi.adapter<List<HiddenMeaning>>(
                Types.newParameterizedType(List::class.java, HiddenMeaning::class.java)
            )
            adapter.fromJson(analysis.serializedHiddenMeanings) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Parse serialized takeaways from DB back to logic objects.
     */
    fun getTakeaways(analysis: ChatAnalysis): List<String> {
        return try {
            val adapter = moshi.adapter<List<String>>(
                Types.newParameterizedType(List::class.java, String::class.java)
            )
            adapter.fromJson(analysis.serializedTakeaways) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Fallback high-fidelity sample analysis.
     */
    fun getSampleAnalysis(title: String, rawText: String, sampleIndex: Int = 0): ChatAnalysis {
        val samples = listOf(
            // Sample 1: WhatsApp crush breadcrumbing
            ChatAnalysis(
                title = "Crush on WhatsApp",
                rawText = "Me: Hey, are we still on for tonight? :)\nMe: (3 hours later) Just checking in!\nThem: Hey! Omgg I've been so incredibly busy today, my boss threw this huge project at me last second 😭😭 Can we reschedule for next week?",
                interestScore = 32,
                ghostingProbability = 78,
                emotionalBalanceIndex = -85, // Heavily user invested
                attractionMeter = 21,
                trustScore = 40,
                authenticityScore = 45,
                intentLikelihood = "Quiet Fading",
                powerDynamicRating = "Counterparty Dominated",
                manipulationPattern = "Breadcrumbing",
                attachmentStyle = "Anxious Spiral",
                brutalTruth = "You are priority #42, right behind organizing their bookmarks. The double-text in the afternoon combined with an emoji-stacked excuse is classic energy-milking to preserve attention without actual intent of meeting.",
                nextMove = "Go complete radio silent. Do NOT send another emoji. Reply in 16 hours with a simple 'No worries!' and do not suggest another date. Re-establish mystery immediately.",
                serializedHiddenMeanings = moshi.adapter<List<HiddenMeaning>>(Types.newParameterizedType(List::class.java, HiddenMeaning::class.java)).toJson(
                    listOf(
                        HiddenMeaning("I've been so incredibly busy", "I was actively browsing Instagram but lacked the mental energy to commit to an evening containing you.", "MEDIUM"),
                        HiddenMeaning("Can we reschedule for next week?", "I am putting you in an active holding pattern so I can decide if a better option appears.", "DANGER"),
                        HiddenMeaning("😭😭 [crying emojis]", "Visual sympathy shields to prevent you from being upset while I discard our plans.", "LOW")
                    )
                ),
                serializedTakeaways = moshi.adapter<List<String>>(Types.newParameterizedType(List::class.java, String::class.java)).toJson(
                    listOf(
                        "Stop initiating double texts. It kills leverage.",
                        "Enforce a 3:1 reply-ratio timeout on rescheduled excuses.",
                        "Match their flat-lining investment or walk away completely."
                    )
                )
            ),
            // Sample 2: Tinder match power play
            ChatAnalysis(
                title = "Tinder Match Dynamics",
                rawText = "Them: hey tell me a secret\nMe: haha depends on what kind of secret you're looking for\nThem: only the dangerous ones",
                interestScore = 88,
                ghostingProbability = 12,
                emotionalBalanceIndex = 15, // Balanced/slightly them
                attractionMeter = 92,
                trustScore = 55,
                authenticityScore = 70,
                intentLikelihood = "Casual Adventure",
                powerDynamicRating = "Balanced",
                manipulationPattern = "None (Authentic)",
                attachmentStyle = "Secure Connection",
                brutalTruth = "Extremely high magnetic tension. They are deliberately steering the vibe into high-risk, exciting conversational territory to test your threshold for playfulness. Solid mutual energy.",
                nextMove = "Offer a playful, slightly cocky riddle: 'I'll trade you my secret for your phone number. Standard rate. Deal?' Keep the high-stakes vibe going.",
                serializedHiddenMeanings = moshi.adapter<List<HiddenMeaning>>(Types.newParameterizedType(List::class.java, HiddenMeaning::class.java)).toJson(
                    listOf(
                        HiddenMeaning("tell me a secret", "Bypass standard small talk. I am highly intrigued by you and skipping straight to dopamine-inducing banters.", "LOW"),
                        HiddenMeaning("only the dangerous ones", "Are you basic, or can you match my confidence? I am testing your conversational wit.", "MEDIUM")
                    )
                ),
                serializedTakeaways = moshi.adapter<List<String>>(Types.newParameterizedType(List::class.java, String::class.java)).toJson(
                    listOf(
                        "Leverage the tension immediately before the small-talk gravity pulls you back down.",
                        "Transition to a phone number or offline meet while attraction is peaking.",
                        "Do NOT write a paragraph. High confidence means light touch."
                    )
                )
            ),
            // Sample 3: Passive-aggressive LinkedIn Recruiter
            ChatAnalysis(
                title = "Passive-Aggressive Recruiter",
                rawText = "Recruiter: Hi, we noticed your background and thought you'd be a great fit! Let's chat next Tuesday at 9 AM PST.\nMe: Hi, thank you! I am free on Tuesday but only after 1 PM PST.\nRecruiter: Tuesday morning is the only slot our team has available for this critical hire.",
                interestScore = 55,
                ghostingProbability = 30,
                emotionalBalanceIndex = -40,
                attractionMeter = 10,
                trustScore = 30,
                authenticityScore = 50,
                intentLikelihood = "Casual Adventure",
                powerDynamicRating = "Counterparty Dominated",
                manipulationPattern = "Passive Aggressive",
                attachmentStyle = "Avoidant Distance",
                brutalTruth = "The recruiter is playing a corporate leverage play, asserting dominance early in the negotiation cycle to make you feel fortunate to be interviewed. A severe lack of candidate respect.",
                nextMove = "Keep it ultra-professional but boundaries firm: 'I understand Tuesday morning is tight. If scheduling shifts or afternoon slots open later, please let me know!' Put the ball back in their court.",
                serializedHiddenMeanings = moshi.adapter<List<HiddenMeaning>>(Types.newParameterizedType(List::class.java, HiddenMeaning::class.java)).toJson(
                    listOf(
                        HiddenMeaning("this critical hire", "We are behind schedule and understaffed, but we expect you to adapt entirely to our convenience.", "MEDIUM"),
                        HiddenMeaning("Tuesday morning is the only slot", "I am practicing psychological frame-control to see if you are easily compliant.", "DANGER")
                    )
                ),
                serializedTakeaways = moshi.adapter<List<String>>(Types.newParameterizedType(List::class.java, String::class.java)).toJson(
                    listOf(
                        "Corporate dominance starts at the scheduler. Do not break firm scheduling boundaries.",
                        "Compliance now sets a low anchor for future salary negotiations.",
                        "Be prepared for them to suddenly adapt and offer the afternoon slot anyway."
                    )
                )
            )
        )
        return samples[sampleIndex % samples.size].copy(title = title, rawText = rawText)
    }
}
