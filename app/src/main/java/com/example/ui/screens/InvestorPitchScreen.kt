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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
fun InvestorPitchScreen(viewModel: MainViewModel) {
    val currentSlide by viewModel.currentPitchSlide.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()

    val totalSlides = 12

    val scrollState = rememberScrollState()

    // Smooth scroll back to top when slide changes
    LaunchedEffect(currentSlide) {
        scrollState.animateScrollTo(0)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepCharcoal),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "STARTUP BLUEPRINT DECK",
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
            BottomNavBar(currentScreen = Screen.InvestorPitch, onNavigate = { viewModel.navigateTo(it) }, isPremium = isPremium)
        },
        containerColor = DeepCharcoal
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Slide indicator line
            LinearProgressIndicator(
                progress = { (currentSlide + 1).toFloat() / totalSlides },
                modifier = Modifier.fillMaxWidth().height(4.dp),
                color = BrightNeonGreen,
                trackColor = Color.White.copy(alpha = 0.08f)
            )

            // Main slide layout
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                
                // Chapter counter badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(BrightNeonGreen.copy(alpha = 0.1f))
                        .border(1.dp, BrightNeonGreen.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "BLUEPRINT SECTION ${currentSlide + 1} OF $totalSlides",
                        color = BrightNeonGreen,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Render current Slide
                when (currentSlide) {
                    0 -> SlideCover()
                    1 -> SlideVision()
                    2 -> SlidePersonas()
                    3 -> SlideViralMechanics()
                    4 -> SlideProductFeatures()
                    5 -> SlideAiOutputs()
                    6 -> SlideUserExperience()
                    7 -> SlideMonetization()
                    8 -> SlideCompetition()
                    9 -> SlideInvestorPitch()
                    10 -> SlideTechArchitecture()
                    11 -> SlideBrutalCritique()
                }

                Spacer(modifier = Modifier.height(50.dp))
            }

            // Carousel Slide Controls
            Surface(
                color = SatinSlate,
                tonalElevation = 0.dp, // Flat surface
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.04f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (currentSlide > 0) viewModel.setPitchSlide(currentSlide - 1) },
                        enabled = currentSlide > 0,
                        modifier = Modifier.testTag("pitch_previous_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Previous slide",
                            tint = if (currentSlide > 0) PureWhite else Color.White.copy(alpha = 0.15f),
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Text(
                        text = "CHAPTER ${currentSlide + 1} / $totalSlides",
                        color = SoftGrey,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = { if (currentSlide < totalSlides - 1) viewModel.setPitchSlide(currentSlide + 1) },
                        enabled = currentSlide < totalSlides - 1,
                        modifier = Modifier.testTag("pitch_next_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Next slide",
                            tint = if (currentSlide < totalSlides - 1) PureWhite else Color.White.copy(alpha = 0.15f),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SlideCover() {
    Column {
        Text(
            text = "SEENOFFLINE",
            color = PureWhite,
            fontSize = 36.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.sp
        )
        Text(
            text = "THE CULTURAL PHENOMENON",
            color = BrightNeonGreen,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "SeenOffline is a viral consumer AI application designed to help people decode digital social subtext — the hidden meaning beneath chat interactions. It acts as an objective lens for emotional investments, power dynamics, and conversational leverage.",
            color = SoftGrey,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = SatinSlate),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.04f))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "THE CORE EMOTIONAL TRIGGER",
                    color = PremiumGold,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Modern communication is entirely transactional but highly opaque. Every double text, late reply, and passive emoji represents a micro-transaction of social power. SeenOffline provides instant leverage calculations that create viral anxiety, gossip fuel, and sharing loops.",
                    color = SoftGrey,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun SlideVision() {
    Column {
        Text("1. Product Vision", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("MISSION, BRAND ARCHETYPE & DIFFERENTIATORS", color = BrightNeonGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        BlueprintBullet("Mission Statement", "To make digital communication completely transparent, giving consumers an elite lens of social intelligence to read between conversational lines.")
        BlueprintBullet("Vision Core", "Not a productivity assistant or relationship therapist. SeenOffline is an internet culture artifact — a mirror to digital power plays representing high drama, status calculations, and pure excitement.")
        BlueprintBullet("Brand Archetype", "The ‘Tricking Rebel’ (Outlaw meets Magician). Playful, highly confident, slightly dangerous, and brutally honest. It has the confidence of an elite luxury club host paired with the speed of internet memes.")
        BlueprintBullet("Emotional Territory", "Vulnerabilities of digital rejection and validation. SeenOffline plays directly with the thrill of exposing hidden intentions, attraction validations, and peer benchmarking.")
    }
}

@Composable
fun SlidePersonas() {
    Column {
        Text("2. User Personas", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("PRIMARY & SECONDARY ADOPTERS", color = BrightNeonGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        BlueprintBullet(
            "The Over-Invested Spinner (Primary)",
            "Agen 18-26. Regularly over-analyzes text messages, waiting hours for replies, anxious about double texting. Motivated by reassurance but deeply craving strategic agency."
        )
        BlueprintBullet(
            "The High-Stakes Player (Secondary)",
            "Bumble/Tinder heavy-adopters. High attention volume but wants to optimize reply effectiveness, filter out low-intent prospects, and project high dynamic value."
        )
        BlueprintBullet(
            "The Gossip Ring Leader (Power User)",
            "Natural community curators. Screengrabbing friends' texts to post in group channels. SeenOffline gives them the ultimate shareable tool to roasting friends' conversational execution."
        )
        BlueprintBullet(
            "The TikTok Cultural Seer (Influencer)",
            "Commentators who decode viral cultural happenings. SeenOffline is an infinite engine of dramatic content (running public celebrity DMs/tweets through diagnostics to expose subtext)."
        )
    }
}

@Composable
fun SlideViralMechanics() {
    Column {
        Text("3. Viral Mechanics", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("HOW WE TARGET 1M ACTIVE USERS", color = BrightNeonGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        BlueprintBullet("The Wrapped Story Loop", "Users are given a high-contrast Spotify-Wrapped style share card. This card features a roasty title ('CERTIFIED SYMPATHY REPLIER') and spectacular scores. It is optimized to be shared directly on Instagram or BeReal, creating massive organic loops.")
        BlueprintBullet("The Group Chat Referral", "To unlock deep metrics ('What did they actually mean next?'), users must share the diagnostic result to their group chat or refer one friend. This converts a solo analysis task into a collaborative entertainment session.")
        BlueprintBullet("Creator Loop: Celebrity Audits", "SeenOffline publishes weekly TikTok videos analyzing public, leaked, or legendary celebrity texts (e.g. Elon Musk, Taylor Swift, historic letters) exposing their attraction metrics. This fuels meme accounts and brand alignment.")
        BlueprintBullet("Network Mechanics", "A score benchmark matrix. Users compare their average Attraction Scores and Ghosting Probability against regional averages, fueling competitive status validation.")
    }
}

@Composable
fun SlideProductFeatures() {
    Column {
        Text("4. Product Features", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("MVP TO LONG-TERM PLATFORM EXPANSION", color = BrightNeonGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        BlueprintBullet("MVP Level (The Launch)", "Direct paste of conversational text logs. Advanced diagnostics featuring: attraction score, ghosting danger, power imbalance, brutal roasts, next response playbook, and Wrapped-styled social post export.")
        BlueprintBullet("Version 2.0 (The Scanner)", "One-tap screenshot upload with OCR text extraction. Auto-categorizes screenshot sources (iMessage blue, Green bubbles, WhatsApp, Instagram dark-mode DMs) and color-codes active lines.")
        BlueprintBullet("Version 3.0 (The Keyboard UI)", "A Custom SeenOffline Android IME system keyboard. Analyzes incoming texts on the fly and suggests real-time high-leverage replies directly in Whatsapp or Tinder.")
        BlueprintBullet("Long-Term Platform", "An anonymous social network feed ('SeenOffline Board') where users post cropped text exchanges anonymously, prompting the community and AI to vote on who has the upper hand.")
    }
}

@Composable
fun SlideAiOutputs() {
    Column {
        Text("5. AI Analysis Framework", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("THE REVOLUTIONARY INTENT ENGINE", color = BrightNeonGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        BlueprintBullet("Mojo (Attraction Meter)", "Measures underlying curiosity and chemical interest based on punctuation density, word lengths, speed of response, and question count (0 - 100%).")
        BlueprintBullet("Ghosting Forecast", "Calculates the statistical likelihood of sudden communication blackout inside the next 96 hours based on text flatlining vectors.")
        BlueprintBullet("Investment Imbalance Metric", "Measures character length, line frequency, and speed ratios to show who is over-invested. Scores from -100% (Anxious Overinvestor) to +100% (Unbothered Leveraged).")
        BlueprintBullet("Subtext Translation Dictionary", "Dramatically translates defensive or generic phrases into their true intention (e.g., 'Let's see closer to' -> 'I am checking if secondary options cancel first').")
    }
}

@Composable
fun SlideUserExperience() {
    Column {
        Text("6. User Experience", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("SENSORY DESIGN PRINCIPLES", color = BrightNeonGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        BlueprintBullet("The Atmosphere", "Deep dark charcoal layouts evoking private luxury social clubs. Sharp neon highlights representing real-time status changes. Looks like an A24 movie production rather than a generic SaaS app.")
        BlueprintBullet("Tactile Feedback", "Micro-animations. When analyzing, the indicator ticks like an analog clock, simulating sophisticated, slightly intimidating brain computations.")
        BlueprintBullet("No AI Clichés", "Strictly avoids standard AI blue stars or chat inputs. Everything looks structural, typographic, bold, and high-fashion.")
    }
}

@Composable
fun SlideMonetization() {
    Column {
        Text("7. Monetization Blueprint", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("VIRAL VALUE CAPTURE TIERS", color = BrightNeonGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        BlueprintBullet("Free Tier", "3 diagnostics per week. Includes basic Attraction scores, Next response script, and standard social-card export layouts.")
        BlueprintBullet("Elite Club ($14.99 / Month)", "Unlimited analyses, priority OCR queue, deep subtext translations, historic statistics hub tracking individual crush progress over time.")
        BlueprintBullet("Venture VIP Tier ($49.99 / Year)", "Dedicated AI behavioral coach simulator, weekly social dynamic workshops, elite DeX/desktop dashboard overlays.")
    }
}

@Composable
fun SlideCompetition() {
    Column {
        Text("8. Competition Matrix", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("WHY SEENOFFLINE DOMINATES", color = BrightNeonGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        BlueprintBullet("vs ChatGPT / Claude", "LLMs offer vanilla, safe, academic therapy templates ('Communication is key, assert your feelings...'). SeenOffline gives you tactical, witty, slightly roasty real-world advice to regain leverage in seconds.")
        BlueprintBullet("vs NGL", "NGL is anonymous Q&A. SeenOffline is an active utility analyzing real-world relationships. High utility creates long-term active retention.")
        BlueprintBullet("vs Reddit Advice (RelationshipDirs)", "Reddit requires hours of waiting for conflicting random replies. SeenOffline gives diagnostic assessments instantly based on advanced psychology profiles.")
    }
}

@Composable
fun SlideInvestorPitch() {
    Column {
        Text("9. Investor Pitch", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("THE INVESTMENT CRITERIA", color = BrightNeonGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        BlueprintBullet("Problem", "Modern GenZ/Millennial communication takes place entirely in short-form text formats loaded with micro-power dynamics, creating massive cognitive friction, anxiety, and hours of redundant counseling in group chats.")
        BlueprintBullet("Solution", "A high-retention, viral social intelligence companion translating subtext instantly and serving as a funny, secure peer validation network.")
        BlueprintBullet("Moat", "Proprietary reinforcement fine-tuning of linguistic subtext dynamics, coupled with high network density in group chat loops.")
        BlueprintBullet("Exit Path", "Acquisition by major social holdings (Match Group, Tinder, Bumble, TikTok) to serve as a high-density consumer utility onboarding engine.")
    }
}

@Composable
fun SlideTechArchitecture() {
    Column {
        Text("10. Technical Architecture", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("RAPID LAUNCH SCALABLE STACK", color = BrightNeonGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        BlueprintBullet("Frontend", "Kotlin Jetpack Compose. Multi-screen state machine, Edge-to-Edge window inset layouts, and SQLite Room for local history cache.")
        BlueprintBullet("Backend", "Cloudflare Workers or FastAPI. Ultra low latency edge caching that manages Gemini API calls, striping, and database syncing.")
        BlueprintBullet("AI Processing", "Gemini 3.5 Flash running under strict structured JSON schemas for stable, low-latency, low-cost operations.")
        BlueprintBullet("Analytics & Safety", "Mixpanel and App Check (Option A) or client-side telemetry to track viral sharing triggers and ensure user confidentiality.")
    }
}

@Composable
fun SlideBrutalCritique() {
    Column {
        Text("11. Brutal Critique", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("THE UNFILTERED VENTURE ANALYSIS", color = BrightNeonGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        BlueprintBullet("Ethical Risks", "Could foster communication hyper-skepticism or trust issues if users take jokes too literally. We resolve this by ensuring a disclaimer tag: 'Intelligence is entertainment; trust your gut first.'")
        BlueprintBullet("Legal & Privacy Risks", "Uploading third party chats can raise local privacy questions. Since data is cached locally on device via SQLite Room first, privacy is guaranteed. Core API transmissions are anonymized and non-identifiable.")
        BlueprintBullet("The Bubble Risk", "Can it resist being just another viral gimmick? Yes — by continuously expanding into keyboard utility additions, historical stats dashboards, and community voting boards.")
        BlueprintBullet("Venture Summary", "SeenOffline is a high-reward consumer play. If executed with extreme creative direction (high A24 aesthetic precision) and brutally funny roasts, it becomes a multi-million-user pipeline almost instantly.")
    }
}

@Composable
fun BlueprintBullet(
    title: String,
    body: String
) {
    Column(modifier = Modifier.padding(vertical = 10.dp)) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .offset(y = 8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(BrightNeonGreen)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title.uppercase(),
                    color = PureWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = body,
                    color = SoftGrey,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
