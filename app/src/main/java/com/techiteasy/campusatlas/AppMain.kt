package com.techiteasy.campusatlas

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.techiteasy.campusatlas.ui.map.Mapview
import com.techiteasy.campusatlas.ui.panels.BookmarksScreen
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

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (isFirstRun) "setup" else "map"
    ) {
        composable("setup") {
            FirstSetupScreen(
                onFinish = {
                    sharedPreferences.edit {
                        putBoolean("is_first_run", false)
                    }
                    navController.navigate("map") {
                        popUpTo("setup") { inclusive = true }
                    }
                }
            )
        }

        composable("map") {
            Mapview(navController = navController)
        }

        composable("bookmarks") {
            BookmarksScreen()
        }

        composable("settings") {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
