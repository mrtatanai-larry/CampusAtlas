package com.techiteasy.campusatlas

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.techiteasy.campusatlas.ui.auth.CodePanel
import com.techiteasy.campusatlas.ui.map.MapPanel
import com.techiteasy.campusatlas.ui.panels.BookmarksPanel


@Composable
fun AppNav() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "code"
    ) {

        composable("code") {
            CodePanel(
                onContinue = {
                    navController.navigate("map")
                }
            )
        }

        composable("map") {
            MapPanel(navController = navController)
        }

        composable("bookmarks") {
            BookmarksPanel(
                navController = navController
            )
        }

    }
}
