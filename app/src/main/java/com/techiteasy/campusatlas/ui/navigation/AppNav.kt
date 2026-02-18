package com.techiteasy.campusatlas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.techiteasy.campusatlas.ui.auth.CodePanel
import com.techiteasy.campusatlas.ui.map.MapPanel

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
            MapPanel()
        }
    }
}

