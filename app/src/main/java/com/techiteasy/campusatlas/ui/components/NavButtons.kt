package com.techiteasy.campusatlas.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.techiteasy.campusatlas.R

@Composable
fun NavButtons(
    navController: NavController,
    currentScreen: String,
    modifier: Modifier = Modifier,
    isAdminMode: Boolean = false,
    onMapClick: (() -> Unit)? = null,
    onBookmarksClick: (() -> Unit)? = null,
    onDataTableClick: (() -> Unit)? = null
) {
    NavigationBar(modifier = modifier, tonalElevation = 4.dp) {
        if (isAdminMode) {
            // Editor Map Button (Only for Admin Mode)
            NavigationBarItem(
                selected = currentScreen == "map",
                onClick = {
                    if (onMapClick != null) {
                        onMapClick()
                    } else {
                        if (navController.currentDestination?.route != "admin_map") {
                            navController.navigate("admin_map") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
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
                    if (onDataTableClick != null) {
                        onDataTableClick()
                    } else {
                        if (navController.currentDestination?.route != "datatable") {
                            navController.navigate("datatable") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
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
                    if (onMapClick != null) {
                        onMapClick()
                    } else {
                        if (navController.currentDestination?.route != "map") {
                            navController.navigate("map") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
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
                    if (onBookmarksClick != null) {
                        onBookmarksClick()
                    } else {
                        if (navController.currentDestination?.route != "bookmarks") {
                            navController.navigate("bookmarks") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                },
                icon = { Icon(painterResource(R.drawable.ic_bookmark), contentDescription = "Bookmarks") },
                label = { Text("Bookmarks") }
            )
        }
    }
}
