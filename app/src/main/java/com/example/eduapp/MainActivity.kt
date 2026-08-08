@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.eduapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eduapp.screen.GameScreen
import com.example.eduapp.screen.LandingScreen
import com.example.eduapp.screen.LevelScreen
import com.example.eduapp.screen.ScoreScreen
import com.example.eduapp.screen.SettingScreen
import com.example.eduapp.screen.StatisticsScreen
import com.example.eduapp.screen.TestDBScreen
import com.example.eduapp.screen.WelcomeScreen
import com.example.eduapp.ui.theme.EduAppTheme
import com.example.eduapp.viewmodel.AppViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val currentContext = applicationContext
        setContent {
            EduAppTheme {
                AppNav(currentContext)
            }
        }
    }
}

@Composable
fun AppNav(currentContext: Context) {
    val navController = rememberNavController()
    val viewModel: AppViewModel = koinViewModel()
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = if (settings.hasUsername) "landing" else "welcome"
    ) {
        composable("welcome") { WelcomeScreen(navController) }
        composable("landing") { LandingScreen(navController) }
        composable("setting") { SettingScreen(navController) }
        composable("levels") { LevelScreen(navController) }
        composable("statistics") { StatisticsScreen(navController) }
        composable(
            route = "game/{level}",
            arguments = listOf(navArgument("level") { type = NavType.IntType })
        ) { backStackEntry ->
            GameScreen(navController, backStackEntry.arguments?.getInt("level") ?: 1)
        }
        composable(
            route = "score/{level}/{score}/{total}/{seconds}",
            arguments = listOf(
                navArgument("level") { type = NavType.IntType },
                navArgument("score") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType },
                navArgument("seconds") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments
            ScoreScreen(
                navController = navController,
                level = args?.getInt("level") ?: 1,
                score = args?.getInt("score") ?: 0,
                total = args?.getInt("total") ?: 0,
                seconds = args?.getInt("seconds") ?: 0
            )
        }
        composable("testDB") { TestDBScreen() }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EduAppTheme {

    }
}
