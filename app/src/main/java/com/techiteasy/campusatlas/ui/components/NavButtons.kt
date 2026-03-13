package com.techiteasy.campusatlas.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techiteasy.campusatlas.R

@Composable
fun NavButtons(
    navController: NavController,
    currentScreen: String,
    onBookmarksClick: () -> Unit,
    onMapClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier, tonalElevation = 4.dp) {

        NavigationBarItem(
            selected = currentScreen == "map",
            onClick = {
                onMapClick()
                if (navController.currentDestination?.route != "map") {
                    navController.navigate("map") {
                        popUpTo("map") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            },
            icon = { Icon(painterResource(R.drawable.ic_explore), contentDescription = "Map View") },
            label = { Text("Map View") }
        )

        NavigationBarItem(
            selected = currentScreen == "bookmarks",
            onClick = {
                if (currentScreen != "bookmarks") {
                    onBookmarksClick()
                }
            },
            icon = { Icon(painterResource(R.drawable.ic_bookmark), contentDescription = "Bookmarks") },
            label = { Text("Bookmarks") }
        )
    }
}
