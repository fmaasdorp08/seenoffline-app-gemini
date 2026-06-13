@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ChatAnalysis
import com.example.network.GeminiHelper
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun AnalysisDetailScreen(viewModel: MainViewModel) {
    val analysis by viewModel.selectedAnalysis.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCharcoal),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ASSESSMENT REVEALED",
                        color = PureWhite,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = PureWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepCharcoal), // Monolithic zero elevation
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            BottomNavBar(currentScreen = Screen.Home, onNavigate = { viewModel.navigateTo(it) }, isPremium = isPremium)
        },
        containerColor = DeepCharcoal
    ) { innerPadding ->
        if (analysis == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .wrapContentSize(Alignment.Center)
            ) {
                Text("Analyzing signal history...", color = SoftGrey, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
            }
        } else {
            val item = analysis!!
            val hiddenMeanings = remember(item) { GeminiHelper.getHiddenMeanings(item) }
            val takeaways = remember(item) { GeminiHelper.getTakeaways(item) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DeepCharcoal)
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Column {
                        Text(
                            text = "CONVERSATION INTELLIGENCE SUBJECT",
                            color = SoftGrey,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = item.title,
                            color = PureWhite,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = (-1).sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        // Indicators panel
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            InfoBadge(label = "DYNAMIC: ${item.powerDynamicRating.uppercase()}", color = BrightNeonGreen)
                            InfoBadge(label = "INTENT: ${item.intentLikelihood.uppercase()}", color = PremiumGold)
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Big Stats Gauge Container
                    Text(
                        text = "VITAL SIGNALS",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Stat Card 1: Attraction Score
                        StatCircleCard(
                            label = "Attraction Score",
                            score = item.interestScore,
                            modifier = Modifier.weight(1f),
                            color = BrightNeonGreen
                        )

                        // Stat Card 2: Ghosting Risk
                        StatCircleCard(
                            label = "Ghosting Probability",
                            score = item.ghostingProbability,
                            modifier = Modifier.weight(1f),
                            color = if (item.ghostingProbability > 50) HotCrimson else BrightNeonGreen
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Stat Card 3: Emotional Investment
                        StatCircleCard(
                            label = "Your Investment Imbalance",
                            score = item.emotionalBalanceIndex,
                            isImbalance = true,
                            modifier = Modifier.weight(1f),
                            color = if (item.emotionalBalanceIndex < -25) HotCrimson else BrightNeonGreen
                        )

                        // Stat Card 4: Trust Score
                        StatCircleCard(
                            label = "Subtext Authenticity",
                            score = item.authenticityScore,
                            modifier = Modifier.weight(1f),
                            color = BrightNeonGreen
                        )
                    }

                    // Dynamic Spotify Wrapped Style Share Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        BrightNeonGreen.copy(alpha = 0.15f),
                                        PremiumGold.copy(alpha = 0.05f)
                                    )
                                )
                            )
                            .border(1.dp, BrightNeonGreen.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                            .clickable { viewModel.navigateTo(Screen.ShareCard) }
                            .padding(20.dp)
                            .testTag("detail_share_banner")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "VIRAL WRAPPED CARD AVAILABLE",
                                    color = BrightNeonGreen,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Generate a social-first shareable card for TikTok, BeReal, or Instagram Stories.",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = BrightNeonGreen)
                        }
                    }

                    // BRUTAL TRUTH (The Brutal Read - high-contrast uncensored white card from Clean Minimalism spec)
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("brutal_truth_card")
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "THE BRUTAL READ",
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.Black)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        "UNCENSORED",
                                        color = Color.White,
                                        fontSize = 8.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = item.brutalTruth,
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    // THE TRANSLATION BOARD
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "SUBTEXT TRANSLATION SERVICE",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        hiddenMeanings.forEach { meaning ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = SatinSlate),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "“${meaning.originalPhrase}”",
                                            color = Color.White,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontStyle = FontStyle.Italic,
                                            modifier = Modifier.weight(1f)
                                        )
                                        
                                        Spacer(modifier = Modifier.width(12.dp))
                                        
                                        val badgeColor = when (meaning.severity) {
                                            "DANGER" -> HotCrimson
                                            "MEDIUM" -> PremiumGold
                                            else -> BrightNeonGreen
                                        }
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(badgeColor.copy(alpha = 0.1f))
                                                .border(1.dp, badgeColor.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = meaning.severity,
                                                color = badgeColor,
                                                fontSize = 8.sp,
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(verticalAlignment = Alignment.Top) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            tint = BrightNeonGreen,
                                            modifier = Modifier.size(16.dp).offset(y = 2.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Actual meaning: ${meaning.translation}",
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontSize = 13.sp,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // STRATEGIC REACTION PLAYBOOK
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SatinSlate),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "TACTICAL PLAYBOOK",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "HOW TO REPLY:",
                                color = BrightNeonGreen,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = item.nextMove,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = Color.White.copy(alpha = 0.05f))
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "LONG TERM ACTIONS:",
                                color = PremiumGold,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            takeaways.forEachIndexed { i, takeaway ->
                                Row(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "${i + 1}.",
                                        color = PremiumGold,
                                        fontSize = 13.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = takeaway,
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 13.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun InfoBadge(label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.1f))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = color,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun StatCircleCard(
    label: String,
    score: Int,
    isImbalance: Boolean = false,
    modifier: Modifier = Modifier,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SatinSlate),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), // Thin high-vibe border
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = label.uppercase(),
                color = SoftGrey,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                maxLines = 1
            )
            
            val scoreStr = if (isImbalance) {
                if (score < 0) "$score%" else "+$score%"
            } else {
                "$score%"
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = scoreStr,
                    color = PureWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Monospace
                )
                
                // Add mini visual indicator tag matching html styling
                val statusLabel = when {
                    isImbalance -> if (score < -25) "Imbalanced" else "Optimal"
                    label.contains("Ghosting", ignoreCase = true) -> if (score > 55) "Crit" else "Low"
                    label.contains("Attraction", ignoreCase = true) -> if (score > 70) "High" else "Soft"
                    else -> if (score > 70) "Stable" else "Mild"
                }

                val statusColor = if (statusLabel == "Crit" || statusLabel == "Imbalanced") HotCrimson else BrightNeonGreen

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(statusColor.copy(alpha = 0.08f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = statusLabel.uppercase(),
                        color = statusColor,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
