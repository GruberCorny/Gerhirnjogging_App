package com.example.gehirnjogingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gehirnjogingapp.ui.DobbleScreen
import com.example.gehirnjogingapp.ui.MemoryScreen
import com.example.gehirnjogingapp.ui.StartScreen
import com.example.gehirnjogingapp.ui.theme.GehirnJogingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GehirnJogingAppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            StartScreen(
                onGameClick = { gameTitle ->
                    when (gameTitle) {
                        "Gedächtnis" -> navController.navigate("memory")
                        "Dobble" -> navController.navigate("dobble")
                    }
                }
            )
        }
        composable("memory") {
            MemoryScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("dobble") {
            DobbleScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}