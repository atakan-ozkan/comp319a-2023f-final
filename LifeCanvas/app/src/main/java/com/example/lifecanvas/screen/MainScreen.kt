package com.example.lifecanvas.screen

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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lifecanvas.viewModel.UserViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController,userViewModel: UserViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Welcome ${userViewModel.getFirstName()}") },
                actions = {
                    IconButton(onClick = { /* TODO: Handle settings click */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = { /* TODO: Show menu options */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                    }
                }
            )
        }
    ) {
            innerPadding ->
        Column(modifier = Modifier.padding(innerPadding),verticalArrangement = Arrangement.spacedBy(16.dp)) {
            FeatureCard(featureName = "Notes", onClick = { navController.navigate("notesScreen") })
            FeatureCard(featureName = "Calendar", onClick = { navController.navigate("calendarScreen")})
            FeatureCard(featureName = "Sketch", onClick = { navController.navigate("sketchScreen")})
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
            modifier = Modifier.padding(16.dp) // Padding inside each card
        ) {
            Text(text = featureName, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
