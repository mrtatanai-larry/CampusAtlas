package com.techiteasy.campusatlas

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.techiteasy.campusatlas.ui.mainscreen.Mapview
import com.techiteasy.campusatlas.ui.panels.BookmarksScreen
import com.techiteasy.campusatlas.ui.panels.DataTableScreen
import com.techiteasy.campusatlas.ui.panels.SettingsScreen
import com.techiteasy.campusatlas.ui.setup.FirstSetupScreen

@Composable
fun AppMain() {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("campus_atlas_prefs", Context.MODE_PRIVATE)
    }
    
    // Check if it's the first run to determine start destination
    val isFirstRun = remember {
        sharedPreferences.getBoolean("is_first_run", true)
    }

    // Persist isAdminMode globally
    var isAdminMode by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_admin_mode", false))
    }

    LaunchedEffect(isAdminMode) {
        sharedPreferences.edit {
            putBoolean("is_admin_mode", isAdminMode)
        }
    }

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (isFirstRun) "setup" else if (isAdminMode) "admin_map" else "map"
    ) {
        composable("setup") {
            FirstSetupScreen(
                onFinish = { isAdminSelected ->
                    isAdminMode = isAdminSelected
                    sharedPreferences.edit {
                        putBoolean("is_first_run", false)
                    }
                    val destination = if (isAdminSelected) "admin_map" else "map"
                    navController.navigate(destination) {
                        popUpTo("setup") { inclusive = true }
                    }
                }
            )
        }

        composable("map") {
            Mapview(
                navController = navController,
                isAdminMode = false
            )
        }

        composable("admin_map") {
            Mapview(
                navController = navController,
                isAdminMode = true
            )
        }

        composable("datatable") {
            DataTableScreen(navController = navController)
        }

        composable("bookmarks") {
            BookmarksScreen()
        }

        composable("settings") {
            SettingsScreen(
                isAdminMode = isAdminMode,
                onAdminModeChange = { newAdminMode -> 
                    isAdminMode = newAdminMode
                    val destination = if (newAdminMode) "admin_map" else "map"
                    navController.navigate(destination) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
