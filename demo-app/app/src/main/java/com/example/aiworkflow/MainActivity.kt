package com.example.aiworkflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aiworkflow.core.designsystem.AiWorkflowTheme
import com.example.aiworkflow.feature.benchmark.BenchmarkScreen
import com.example.aiworkflow.feature.hub.HubScreen
import com.example.aiworkflow.feature.playbook.PlaybookScreen
import com.example.aiworkflow.feature.release.ReleaseScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AiWorkflowTheme {
                AppNavHost()
            }
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "hub") {
        composable("hub") {
            HubScreen(
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable("playbook") {
            PlaybookScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("benchmark") {
            BenchmarkScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("release") {
            ReleaseScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
