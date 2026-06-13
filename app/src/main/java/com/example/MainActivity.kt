package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

                Crossfade(
                    targetState = currentScreen,
                    label = "ScreenTransition",
                    modifier = Modifier.fillMaxSize()
                ) { screen ->
                    when (screen) {
                        is Screen.Onboarding -> OnboardingScreen(viewModel)
                        is Screen.Home -> HomeScreen(viewModel)
                        is Screen.AnalysisInput -> AnalysisInputScreen(viewModel)
                        is Screen.AnalysisDetail -> AnalysisDetailScreen(viewModel)
                        is Screen.ShareCard -> ShareCardScreen(viewModel)
                        is Screen.PremiumUnlock -> PremiumUnlockScreen(viewModel)
                        is Screen.InvestorPitch -> InvestorPitchScreen(viewModel)
                    }
                }
            }
        }
    }
}
