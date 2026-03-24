package com.techiteasy.campusatlas

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.techiteasy.campusatlas.ui.components.NavButtons
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

    // track if user chose admin mode on startup
    var wasInitiallyAdmin by remember {
        mutableStateOf(sharedPreferences.getBoolean("was_initially_admin", false))
    }

    LaunchedEffect(isAdminMode) {
        sharedPreferences.edit {
            putBoolean("is_admin_mode", isAdminMode)
        }
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Professional Animation Configuration
    val animDuration = 400
    val animEasing = FastOutSlowInEasing

    Scaffold(
        bottomBar = {
            // Only show NavButtons on main screens and if not first setup
            if (!isFirstRun && currentRoute != "setup" && currentRoute != "settings") {
                NavButtons(
                    navController = navController,
                    currentScreen = when(currentRoute) {
                        "admin_map", "map" -> "map"
                        "datatable" -> "datatable"
                        "bookmarks" -> "bookmarks"
                        else -> "map"
                    },
                    isAdminMode = isAdminMode
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isFirstRun) "setup" else if (isAdminMode) "admin_map" else "map",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("setup") {
                FirstSetupScreen(
                    onFinish = { isAdminSelected ->
                        isAdminMode = isAdminSelected
                        wasInitiallyAdmin = isAdminSelected
                        sharedPreferences.edit {
                            putBoolean("is_first_run", false)
                            putBoolean("was_initially_admin", isAdminSelected)
                        }
                        val destination = if (isAdminSelected) "admin_map" else "map"
                        navController.navigate(destination) {
                            popUpTo("setup") { inclusive = true }
                        }
                    }
                )
            }

            composable(
                "map",
                enterTransition = { fadeIn(animationSpec = tween(animDuration)) },
                exitTransition = { fadeOut(animationSpec = tween(animDuration)) },
                popEnterTransition = { fadeIn(animationSpec = tween(animDuration)) },
                popExitTransition = { fadeOut(animationSpec = tween(animDuration)) }
            ) {
                Mapview(
                    navController = navController,
                    isAdminMode = false
                )
            }

            composable(
                "admin_map",
                enterTransition = { fadeIn(animationSpec = tween(animDuration)) },
                exitTransition = { fadeOut(animationSpec = tween(animDuration)) },
                popEnterTransition = { fadeIn(animationSpec = tween(animDuration)) },
                popExitTransition = { fadeOut(animationSpec = tween(animDuration)) }
            ) {
                Mapview(
                    navController = navController,
                    isAdminMode = true
                )
            }

            composable(
                "datatable",
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(animDuration, easing = animEasing)
                    ) + fadeIn(animationSpec = tween(animDuration))
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(animDuration, easing = animEasing)
                    ) + fadeOut(animationSpec = tween(animDuration))
                }
            ) {
                DataTableScreen(navController = navController)
            }

            composable(
                "bookmarks",
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(animDuration, easing = animEasing)
                    ) + fadeIn(animationSpec = tween(animDuration))
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(animDuration, easing = animEasing)
                    ) + fadeOut(animationSpec = tween(animDuration))
                }
            ) {
                BookmarksScreen()
            }

            composable(
                "settings",
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(animDuration, easing = animEasing)
                    ) + fadeIn(animationSpec = tween(animDuration))
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(animDuration, easing = animEasing)
                    ) + fadeOut(animationSpec = tween(animDuration))
                }
            ) {
                SettingsScreen(
                    navController = navController,
                    isAdminMode = isAdminMode,
                    canSwitchAdminMode = wasInitiallyAdmin,
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
}
