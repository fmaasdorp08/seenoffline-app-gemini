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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun PremiumUnlockScreen(viewModel: MainViewModel) {
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()
    var selectedTier by remember { mutableStateOf(1) } // Default: Monthly

    val tiers = listOf(
        PremiumTier("Weekly access", "$4.99", "/ week", "Billed weekly. Perfect for weekend deconstructs."),
        PremiumTier("Elite Club Monthly", "$14.99", "/ month", "Billed monthly. Save 25%. Most popular choice."),
        PremiumTier("Venture VIP Annual", "$49.99", "/ year", "Billed annually. Save 70%. Lifetime leverage.")
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCharcoal),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ELITE ACCESS",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepCharcoal), // Monolithic header integration
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            BottomNavBar(currentScreen = Screen.PremiumUnlock, onNavigate = { viewModel.navigateTo(it) }, isPremium = isPremium)
        },
        containerColor = DeepCharcoal
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Premium Crown/Star badge
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(50))
                    .border(2.dp, PremiumGold, RoundedCornerShape(50))
                    .background(PremiumGold.copy(alpha = 0.1f))
                    .wrapContentSize(Alignment.Center)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = PremiumGold,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SeenOffline Elite",
                color = PureWhite,
                fontSize = 26.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = (-0.5).sp
            )

            Text(
                text = "Unrestricted psychological scanning with real-time intent forecasts.",
                color = SoftGrey,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp, bottom = 24.dp)
            )

            // Current Premium Status Badge
            if (isPremium) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(PremiumGold.copy(alpha = 0.12f))
                        .border(1.dp, PremiumGold, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = PremiumGold)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("STATUS: ELITE CLUB CLIENT", color = PremiumGold, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            Text("Your diagnostic queries are routed through our priority AI clusters.", color = PureWhite, fontSize = 11.sp, lineHeight = 16.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { viewModel.togglePremiumSimulator(false) },
                    colors = ButtonDefaults.buttonColors(containerColor = HotCrimson),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text("CANCEL TRIAL SUBSCRIPTION", color = PureWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                }
            } else {
                // Feature Matrix List
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PremiumFeatureRow("Unlimited chat diagnostics (bypass queue constraints)")
                    PremiumFeatureRow("Access to 'DANGER' translated hidden definitions")
                    PremiumFeatureRow("Witty responder generator (tailored scripts to win back leverage)")
                    PremiumFeatureRow("Priority high-speed Gemini API pipeline")
                }

                // Tier Selection Column
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    tiers.forEachIndexed { index, tier ->
                        TierChoiceCard(
                            tier = tier,
                            isSelected = selectedTier == index,
                            onClick = { selectedTier = index }
                        )
                    }
                }

                // Subscribe CTA (Vibrant Accent, bold letterspacing, clean rounded container)
                Button(
                    onClick = {
                        viewModel.togglePremiumSimulator(true)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumGold),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("action_premium_subscribe")
                ) {
                    Text(
                        text = "JOIN SEENOFFLINE ELITE CLUB",
                        color = DeepCharcoal,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun PremiumFeatureRow(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = BrightNeonGreen, // Modern neon accent indicator
            modifier = Modifier.size(16.dp).offset(y = 2.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            color = SoftGrey,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}

@Composable
fun TierChoiceCard(
    tier: PremiumTier,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) PremiumGold else Color.White.copy(alpha = 0.05f)
    val bgColor = if (isSelected) PremiumGold.copy(alpha = 0.04f) else SatinSlate

    Card(
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, borderColor),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tier.title.uppercase(),
                    color = PureWhite,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = tier.price,
                        color = PremiumGold,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = tier.period,
                        color = SoftGrey,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = tier.description,
                color = SoftGrey,
                fontSize = 11.sp,
                lineHeight = 14.sp
            )
        }
    }
}

data class PremiumTier(
    val title: String,
    val price: String,
    val period: String,
    val description: String
)
