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
    modifier: Modifier = Modifier,
    onMapClick: () -> Unit = {},
    onBookmarksClick: () -> Unit = {},
    onDataTableClick: () -> Unit = {},
    isAdminMode: Boolean = false
) {
    NavigationBar(modifier = modifier, tonalElevation = 4.dp) {

        if (isAdminMode) {
            // Editor Map Button (Only for Admin Mode)
            NavigationBarItem(
                selected = currentScreen == "map",
                onClick = {
                    onMapClick()
                    if (navController.currentDestination?.route != "admin_map") {
                        navController.navigate("admin_map") {
                            popUpTo("admin_map") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                icon = { Icon(painterResource(R.drawable.ic_explore), contentDescription = "Map Editor") },
                label = { Text("Map Editor") }
            )

            // Data Table Button (Only for Admin Mode)
            NavigationBarItem(
                selected = currentScreen == "datatable",
                onClick = {
                    onDataTableClick()
                    if (navController.currentDestination?.route != "datatable") {
                        navController.navigate("datatable") {
                            launchSingleTop = true
                        }
                    }
                },
                icon = { Icon(painterResource(R.drawable.database_24px), contentDescription = "Data Table") },
                label = { Text("Data Table") }
            )
        } else {
            // Standard Map View Button (Only for User Mode)
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

            // Bookmarks Button (Only for User Mode)
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
}
