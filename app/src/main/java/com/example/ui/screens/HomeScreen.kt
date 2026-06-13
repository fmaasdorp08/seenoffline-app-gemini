package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.BorderStroke
import com.example.data.model.ChatAnalysis
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.Screen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val analyses by viewModel.allAnalyses.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()

    // Calculate aggregated metrics from real user's database history!
    val hasHistory = analyses.isNotEmpty()
    val avgInterest = if (hasHistory) analyses.map { it.interestScore }.average().toInt() else 0
    val avgGhosting = if (hasHistory) analyses.map { it.ghostingProbability }.average().toInt() else 0
    val avgEmotionalBalance = if (hasHistory) analyses.map { it.emotionalBalanceIndex }.average().toInt() else 0

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCharcoal),
        bottomBar = {
            BottomNavBar(currentScreen = Screen.Home, onNavigate = { viewModel.navigateTo(it) }, isPremium = isPremium)
        },
        containerColor = DeepCharcoal
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 24.dp, bottom = 80.dp)
            ) {
                // Dashboard Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Pulsing green online state dot representation
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(BrightNeonGreen, shape = RoundedCornerShape(50))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "AI ENGINE: ACTIVE",
                                    color = SoftGrey,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 2.sp
                                )
                                if (isPremium) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(PremiumGold.copy(alpha = 0.15f))
                                            .border(1.dp, PremiumGold, RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("ELITE", color = PremiumGold, fontSize = 8.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Sanctuary",
                                color = PureWhite,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Normal,
                                letterSpacing = (-0.5).sp
                            )
                        }

                        IconButton(
                            onClick = { viewModel.navigateTo(Screen.PremiumUnlock) },
                            modifier = Modifier.testTag("action_premium")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Upgrade to Elite",
                                tint = if (isPremium) PremiumGold else SoftGrey.copy(alpha = 0.5f),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // Overall Mojo Statistics Hub
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SatinSlate),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), // Precise minimal hairline
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "HISTORIC COGNITIVE INSIGHTS",
                                color = SoftGrey,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp
                            )
                            
                            Spacer(modifier = Modifier.height(18.dp))

                            if (hasHistory) {
                                // The Hero Metric representation from Clean Minimalism HTML layout
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1.2f)) {
                                        Text(
                                            text = "ATTRACTION SCORE",
                                            color = SoftGrey,
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.SemiBold,
                                            letterSpacing = 1.sp
                                        )
                                        Row(verticalAlignment = Alignment.Bottom) {
                                            Text(
                                                text = "$avgInterest",
                                                color = PureWhite,
                                                fontSize = 62.sp,
                                                fontWeight = FontWeight.Light,
                                                letterSpacing = (-2).sp
                                            )
                                            Text(
                                                text = "%",
                                                color = BrightNeonGreen,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier.padding(bottom = 10.dp, start = 1.dp)
                                            )
                                        }
                                    }

                                    // Other mini metrics stacked as beautiful secondary widgets
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        // Mini Metric 1
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color.White.copy(alpha = 0.02f), RoundedCornerShape(8.dp))
                                                .border(1.dp, Color.White.copy(alpha = 0.04f), RoundedCornerShape(8.dp))
                                                .padding(horizontal = 10.dp, vertical = 6.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("RISK", color = SoftGrey, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                            Text(
                                                "$avgGhosting% GHOST", 
                                                color = if (avgGhosting > 50) HotCrimson else BrightNeonGreen, 
                                                fontSize = 11.sp, 
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        }

                                        // Mini Metric 2
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color.White.copy(alpha = 0.02f), RoundedCornerShape(8.dp))
                                                .border(1.dp, Color.White.copy(alpha = 0.04f), RoundedCornerShape(8.dp))
                                                .padding(horizontal = 10.dp, vertical = 6.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("IMBALANCE", color = SoftGrey, fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                                            val sign = if (avgEmotionalBalance >= 0) "+" else ""
                                            Text(
                                                "$sign$avgEmotionalBalance%", 
                                                color = if (avgEmotionalBalance < -25) HotCrimson else PureWhite, 
                                                fontSize = 11.sp, 
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        }
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(14.dp))
                                
                                // Typographic Quote style representation
                                Text(
                                    text = if (avgEmotionalBalance < -25) {
                                        "\"Highly asymmetrical investment. Reclaim authority immediately.\""
                                    } else {
                                        "\"Pacing profile shows dynamic restraint. You hold the leverage.\""
                                    },
                                    color = SoftGrey,
                                    fontFamily = FontFamily.Serif,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            } else {
                                // Empty Stats State
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = Color.White.copy(alpha = 0.15f),
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Awaiting analytical dataset inputs to plot historic averages.",
                                        color = SoftGrey,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                // CTA Action to Upload
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SatinSlate)
                            .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(16.dp))
                            .clickable { viewModel.navigateTo(Screen.AnalysisInput) }
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "EXPOSE NEW CONVERSATION",
                                    color = BrightNeonGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 1.5.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Submit a snapshot or copied chat transcript to run subtext translation diagnostics.",
                                    color = SoftGrey,
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(BrightNeonGreen)
                                    .wrapContentSize(Alignment.Center)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    tint = DeepCharcoal,
                                    modifier = Modifier.size(22.dp),
                                    contentDescription = "New Scan"
                                )
                            }
                        }
                    }
                }

                // History Title with count
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PREVIOUS REPORT FILES (${analyses.size})",
                            color = SoftGrey,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        if (hasHistory) {
                            Text(
                                text = "WIPE SYSTEM LOGS",
                                color = HotCrimson,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                modifier = Modifier
                                    .clickable { viewModel.clearAllHistory() }
                                    .testTag("action_clear_all")
                            )
                        }
                    }
                }

                if (!hasHistory) {
                    item {
                        // Big beautiful empty state
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SatinSlate),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.04f)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White.copy(alpha = 0.03f))
                                        .wrapContentSize(Alignment.Center)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null,
                                        tint = SoftGrey.copy(alpha = 0.3f),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "LOGS VACANT",
                                    color = PureWhite,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Initialize your first decoder session by running a digital chat analysis.",
                                    color = SoftGrey,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 16.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                            }
                        }
                    }
                } else {
                    items(analyses) { analysis ->
                        AnalysisHistoryCard(
                            analysis = analysis,
                            onSelect = { viewModel.selectAnalysisDirect(analysis) },
                            onDelete = { viewModel.deleteAnalysis(analysis) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnalysisHistoryCard(
    analysis: ChatAnalysis,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    val formatter = remember { SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()) }
    val formattedDate = formatter.format(Date(analysis.timestamp))

    Card(
        colors = CardDefaults.cardColors(containerColor = SatinSlate),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), // Strict minimal line
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("analysis_card_${analysis.id}")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = analysis.title,
                        color = PureWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = formattedDate.uppercase(Locale.getDefault()),
                        color = SoftGrey,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                // Attraction pill - clean, minimalist, high fashion
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(BrightNeonGreen.copy(alpha = 0.1f))
                        .border(1.dp, BrightNeonGreen.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "MOJO ${analysis.interestScore}%",
                        color = BrightNeonGreen,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            
            Text(
                text = analysis.brutalTruth,
                color = PureWhite.copy(alpha = 0.8f),
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (analysis.ghostingProbability > 50) HotCrimson else SoftGrey.copy(alpha = 0.4f),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "GHOST PROBABILITY: ${analysis.ghostingProbability}%",
                        color = SoftGrey,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.5.sp
                    )
                }

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete record",
                    tint = HotCrimson.copy(alpha = 0.4f),
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onDelete() }
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    isPremium: Boolean
) {
    Surface(
        color = SatinSlate,
        tonalElevation = 0.dp,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), // Flat border
        modifier = Modifier.navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tab 1: Home
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "History",
                isActive = currentScreen is Screen.Home,
                onClick = { onNavigate(Screen.Home) }
            )

            // Tab 2: Code/Paste
            BottomNavItem(
                icon = Icons.Default.Add,
                label = "Decoder",
                isActive = currentScreen is Screen.AnalysisInput,
                onClick = { onNavigate(Screen.AnalysisInput) }
            )

            // Tab 3: Secret Deck
            BottomNavItem(
                icon = Icons.Default.List,
                label = "Blueprint",
                isActive = currentScreen is Screen.InvestorPitch,
                onClick = { onNavigate(Screen.InvestorPitch) }
            )

            // Tab 4: Elite Club
            BottomNavItem(
                icon = Icons.Default.Star,
                label = "Elite",
                isActive = currentScreen is Screen.PremiumUnlock,
                tint = if (isPremium) PremiumGold else null,
                onClick = { onNavigate(Screen.PremiumUnlock) }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    tint: Color? = null
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // High fidelity active pill transition from clean minimalism style rule
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(if (isActive) Color.White.copy(alpha = 0.06f) else Color.Transparent)
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint ?: (if (isActive) BrightNeonGreen else SoftGrey.copy(alpha = 0.4f)),
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label.uppercase(Locale.getDefault()),
            color = if (isActive) PureWhite else SoftGrey,
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal,
            letterSpacing = 0.5.sp
        )
    }
}
