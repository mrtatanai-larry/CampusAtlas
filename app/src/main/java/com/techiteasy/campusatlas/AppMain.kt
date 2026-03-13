package com.techiteasy.campusatlas

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.techiteasy.campusatlas.ui.map.Mapview
import com.techiteasy.campusatlas.ui.panels.BookmarksScreen
import com.techiteasy.campusatlas.ui.panels.SettingsScreen


@Composable
fun AppMain() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "map"
    ) {

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
