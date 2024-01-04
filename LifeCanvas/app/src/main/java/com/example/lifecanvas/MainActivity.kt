package com.example.lifecanvas

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lifecanvas.ui.theme.LifeCanvasTheme

class MainActivity : ComponentActivity() {
    private val userViewModel = UserViewModel()
    private val userPreferencesManager = UserPreferencesManager()
    private var isUserValid by mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LifeCanvasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    isUserValid = userPreferencesManager.loadData(userViewModel,this)
                    NavigateStartApp(
                        isUserValid = isUserValid,this)
                }
            }
        }
    }


    @Composable
    fun NavigateStartApp(isUserValid: Boolean, context: Context) {
        val navController = rememberNavController()
        var startDestination = "welcomeScreen"

        if (isUserValid) {
            startDestination = "mainScreen"
        }

        NavHost(navController = navController, startDestination = startDestination) {
            composable("welcomeScreen") { WelcomeScreen(navController) }
            composable("registerScreen") { RegisterScreen(navController, userViewModel,userPreferencesManager,context) }
            composable("mainScreen"){ MainScreen(navController, userViewModel) }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen(navController: NavController,userViewModel: UserViewModel) {

    }
}