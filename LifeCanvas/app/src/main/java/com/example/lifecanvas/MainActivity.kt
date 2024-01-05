package com.example.lifecanvas

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.lifecanvas.database.AppDatabase
import com.example.lifecanvas.repository.NoteRepository
import com.example.lifecanvas.screen.MainScreen
import com.example.lifecanvas.screen.NotesScreen
import com.example.lifecanvas.screen.RegisterScreen
import com.example.lifecanvas.screen.WelcomeScreen
import com.example.lifecanvas.viewModel.UserViewModel
import com.example.lifecanvas.ui.theme.LifeCanvasTheme
import com.example.lifecanvas.viewModel.NoteViewModel

class MainActivity : ComponentActivity() {
    private val userViewModel = UserViewModel()
    private val userPreferencesManager = UserPreferencesManager()
    private var isUserValid by mutableStateOf(false)
    private lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(this, AppDatabase::class.java,
            "AppDB").allowMainThreadQueries().build()
        val noteRepository = NoteRepository(db.noteDao())
        noteViewModel = NoteViewModel(noteRepository)
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
            composable("notesScreen"){ NotesScreen(noteViewModel,navController) }
        }
    }



}