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
import com.example.lifecanvas.migration.MigrationManager
import com.example.lifecanvas.repository.NoteRepository
import com.example.lifecanvas.repository.SketchRepository
import com.example.lifecanvas.screen.MainScreen
import com.example.lifecanvas.screen.NoteDetailScreen
import com.example.lifecanvas.screen.NotesScreen
import com.example.lifecanvas.screen.RegisterScreen
import com.example.lifecanvas.screen.SketchDetailScreen
import com.example.lifecanvas.screen.SketchScreen
import com.example.lifecanvas.screen.WelcomeScreen
import com.example.lifecanvas.viewModel.UserViewModel
import com.example.lifecanvas.ui.theme.LifeCanvasTheme
import com.example.lifecanvas.viewModel.NoteViewModel
import com.example.lifecanvas.viewModel.SketchViewModel

class MainActivity : ComponentActivity() {
    private val userViewModel = UserViewModel()
    private val userPreferencesManager = UserPreferencesManager()
    private var isUserValid by mutableStateOf(false)
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var sketchViewModel: SketchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(this, AppDatabase::class.java,
            "AppDB").addMigrations(MigrationManager.MIGRATION_1_2).allowMainThreadQueries().build()
        val noteRepository = NoteRepository(db.noteDao())
        val sketchRepository = SketchRepository(db.sketchDao())
        noteViewModel = NoteViewModel(noteRepository)
        sketchViewModel = SketchViewModel(sketchRepository)
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
            composable("mainScreen"){ MainScreen(navController, userViewModel,noteViewModel,userPreferencesManager,context) }
            composable("notesScreen"){ NotesScreen(noteViewModel,userViewModel,navController) }
            composable("noteDetailScreen/{noteId}") { backStackEntry ->
                val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull() ?: return@composable
                NoteDetailScreen(noteViewModel, noteId, navController,context)
            }
            composable("sketchesScreen"){ SketchScreen(
                sketchViewModel = sketchViewModel,
                userViewModel = userViewModel,
                navController = navController
            )}
            composable("sketchDetailScreen/{sketchId}") { backStackEntry ->
                val sketchId = backStackEntry.arguments?.getString("sketchId")?.toIntOrNull() ?: return@composable
                SketchDetailScreen(sketchViewModel, sketchId, navController, context)
            }

        }
    }
}
