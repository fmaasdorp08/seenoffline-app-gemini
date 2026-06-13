@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun AnalysisInputScreen(viewModel: MainViewModel) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.analysisError.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()

    var chatTitle by remember { mutableStateOf("") }
    var chatText by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCharcoal),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "LOG AN ASSESSMENT",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepCharcoal), // Monolithic flat header
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            BottomNavBar(currentScreen = Screen.AnalysisInput, onNavigate = { viewModel.navigateTo(it) }, isPremium = isPremium)
        },
        containerColor = DeepCharcoal
    ) { innerPadding ->
        if (isLoading) {
            // Elegant premium full-screen dynamic minimal loader
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DeepCharcoal)
                    .padding(innerPadding)
                    .wrapContentSize(Alignment.Center)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    CircularProgressIndicator(
                        color = BrightNeonGreen,
                        strokeWidth = 2.dp, // Thin, minimal indicator
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "DECRYPTING SUBTEXT...",
                        color = BrightNeonGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Extracting linguistic markers and calculating leverage ratios...",
                        color = SoftGrey,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Error Banner
                if (error != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = HotCrimson.copy(alpha = 0.08f)),
                        border = BorderStroke(1.dp, HotCrimson.copy(alpha = 0.25f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = HotCrimson, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = error ?: "",
                                color = PureWhite,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                // Subtext title
                Text(
                    text = "SPECIFY DATASET LOG",
                    color = SoftGrey,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 1. Title Input (Sleek minimalist style)
                OutlinedTextField(
                    value = chatTitle,
                    onValueChange = { chatTitle = it },
                    label = { Text("Conversation ID (e.g. Sarah / Tinder)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrightNeonGreen,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.08f),
                        focusedLabelColor = BrightNeonGreen,
                        unfocusedLabelColor = SoftGrey,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("chat_title_field")
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 2. Chat Log Text Body
                OutlinedTextField(
                    value = chatText,
                    onValueChange = { chatText = it },
                    label = { Text("Paste text dialogues...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrightNeonGreen,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.08f),
                        focusedLabelColor = BrightNeonGreen,
                        unfocusedLabelColor = SoftGrey,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite
                    ),
                    shape = RoundedCornerShape(14.dp),
                    minLines = 5,
                    maxLines = 10,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("chat_text_field")
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Fast Presets / Simulators
                Text(
                    text = "SELECT AN ACTIVE COGNITIVE SIMULATION",
                    color = SoftGrey,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PresetBadge(
                        label = "🍞 Breadcrumb",
                        color = PureWhite, // Minimalist high contrast badge style
                        onClick = {
                            chatTitle = "Breadcrumbing Target"
                            chatText = "Me: Hey, are we still on for tonight? :)\nMe: (3 hours later) Just checking in!\nThem: Hey! Omgg I've been so incredibly busy today, my boss threw this huge project at me last second 😭😭 Can we reschedule for next week?"
                        }
                    )
                    PresetBadge(
                        label = "🔥 Spicy Match",
                        color = BrightNeonGreen,
                        onClick = {
                            chatTitle = "Spicy Interlocking Banter"
                            chatText = "Them: hey tell me a secret\nMe: haha depends on what kind of secret you're looking for\nThem: only the dangerous ones"
                        }
                    )
                    PresetBadge(
                        label = "💼 Power Play",
                        color = PremiumGold,
                        onClick = {
                            chatTitle = "Professional Frame Play"
                            chatText = "Recruiter: Hi, we noticed your background and thought you'd be a great fit! Let's chat next Tuesday at 9 AM PST.\nMe: Hi, thank you! I am free on Tuesday but only after 1 PM PST.\nRecruiter: Tuesday morning is the only slot our team has available for this critical hire."
                        }
                    )
                }

                // Analyze Trigger button - high power Clean Minimalism look: vibrant background and bold monospaced typography
                Button(
                    onClick = {
                        viewModel.performChatAnalysis(chatTitle, chatText)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrightNeonGreen),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("submit_analysis_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "DECODE INSTANTLY",
                            color = DeepCharcoal,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = DeepCharcoal, modifier = Modifier.size(14.dp))
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
                
                // OCR simulation hint with custom styling
                Card(
                    colors = CardDefaults.cardColors(containerColor = SatinSlate),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.04f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = SoftGrey.copy(alpha = 0.6f), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Sandbox Operations: If no Gemini API Key is configured in user secrets, SeenOffline utilizes its offline cognitive analyzer safely to generate high-fidelity subtext diagnostic simulations.",
                            color = SoftGrey,
                            fontSize = 10.sp,
                            lineHeight = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun PresetBadge(
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.05f))
            .border(1.dp, color.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}
