package com.example.lifecanvas.screen

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lifecanvas.UserPreferencesManager
import com.example.lifecanvas.viewModel.NoteViewModel
import com.example.lifecanvas.viewModel.UserViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController,
               userViewModel: UserViewModel,
               noteViewModel: NoteViewModel,
               userPreferencesManager: UserPreferencesManager,
               context: Context) {
    var showMenu by remember { mutableStateOf(false) }
    var agreeAlertDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Welcome ${userViewModel.getFirstName()}") },
                actions = {
                    IconButton(onClick = { /* TODO: Handle settings click */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Delete Account") },
                            onClick = {
                                showMenu = false
                                agreeAlertDialog =true
                            })
                    }
                }
            )
        }
    ) {
            innerPadding ->
        Column(modifier = Modifier.padding(innerPadding),verticalArrangement = Arrangement.spacedBy(16.dp)) {
            FeatureCard(featureName = "Notes", onClick = { navController.navigate("notesScreen") })
            FeatureCard(featureName = "Calendar", onClick = { navController.navigate("calendarScreen")})
            FeatureCard(featureName = "Sketch", onClick = { navController.navigate("sketchesScreen")})
            if(agreeAlertDialog){
                ShowAgreeAlert(
                    onDismiss = {agreeAlertDialog = false},
                    onClickAgree = {
                        agreeAlertDialog = false
                        DeleteUserAndContents(userViewModel,userPreferencesManager,noteViewModel,context)
                        navController.navigate("welcomeScreen")
                    }

                )
            }
        }
    }
}

@Composable
fun FeatureCard(featureName: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = featureName, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ShowAgreeAlert(
    onDismiss: () -> Unit,
    onClickAgree: () -> Unit){

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Are you sure") },
        text = {
            Text(text = "You are deleting you account with your saved contents, are you sure?")
        },
        confirmButton = {
            Button(
                onClick = {
                    onClickAgree()
                    onDismiss()
                }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("No")
            }
        }
    )
}

fun DeleteUserAndContents(
    userViewModel: UserViewModel,
    userPreferencesManager: UserPreferencesManager,
    noteViewModel: NoteViewModel,
    context: Context){
    userViewModel.resetUser()
    userPreferencesManager.deleteUser(context)
    noteViewModel.deleteAllNotes()

}

