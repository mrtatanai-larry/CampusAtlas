package com.techiteasy.campusatlas

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import com.techiteasy.campusatlas.ui.panels.DataTableScreen
import com.techiteasy.campusatlas.ui.panels.SettingsScreen
import com.techiteasy.campusatlas.ui.setup.FirstSetupScreen

@Composable
fun AppMain() {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("campus_atlas_prefs", Context.MODE_PRIVATE)
    }
    
    val isFirstRun = remember {
        sharedPreferences.getBoolean("is_first_run", true)
    }

    var isAdminMode by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_admin_mode", false))
    }

    var wasInitiallyAdmin by remember {
        mutableStateOf(sharedPreferences.getBoolean("was_initially_admin", false))
    }

    // Persist editor mode state
    var isEditorMode by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_editor_mode", true))
    }

    LaunchedEffect(isAdminMode) {
        sharedPreferences.edit {
            putBoolean("is_admin_mode", isAdminMode)
        }
    }

    LaunchedEffect(isEditorMode) {
        sharedPreferences.edit {
            putBoolean("is_editor_mode", isEditorMode)
        }
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val animDuration = 400
    val animEasing = FastOutSlowInEasing

    Scaffold(
        // Set contentWindowInsets to 0 so screens can handle their own edge-to-edge insets
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (currentRoute == "map" || currentRoute == "admin_map" || currentRoute == "datatable" || currentRoute == "bookmarks") {
                NavButtons(
                    navController = navController,
                    currentScreen = if (currentRoute == "datatable") "datatable" else if (currentRoute == "bookmarks") "bookmarks" else "map",
                    isAdminMode = isAdminMode
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isFirstRun) "setup" else if (isAdminMode) "admin_map" else "map",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
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
                enterTransition = {
                    if (initialState.destination.route == "bookmarks" || initialState.destination.route == "admin_map") {
                        EnterTransition.None
                    } else {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(animDuration, easing = animEasing)
                        ) + fadeIn(animationSpec = tween(animDuration))
                    }
                },
                exitTransition = {
                    if (targetState.destination.route == "bookmarks" || targetState.destination.route == "admin_map") {
                        ExitTransition.None
                    } else {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(animDuration, easing = animEasing)
                        ) + fadeOut(animationSpec = tween(animDuration))
                    }
                },
                popEnterTransition = {
                    if (initialState.destination.route == "bookmarks" || initialState.destination.route == "admin_map") {
                        EnterTransition.None
                    } else {
                        fadeIn(animationSpec = tween(animDuration))
                    }
                },
                popExitTransition = {
                    if (targetState.destination.route == "bookmarks" || targetState.destination.route == "admin_map") {
                        ExitTransition.None
                    } else {
                        fadeOut(animationSpec = tween(animDuration))
                    }
                }
            ) {
                Mapview(
                    navController = navController,
                    isAdminMode = false,
                    isEditorMode = isEditorMode,
                    onEditorModeChange = { isEditorMode = it }
                )
            }

            composable(
                "bookmarks",
                enterTransition = {
                    if (initialState.destination.route == "map" || initialState.destination.route == "admin_map") {
                        EnterTransition.None
                    } else {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(animDuration, easing = animEasing)
                        ) + fadeIn(animationSpec = tween(animDuration))
                    }
                },
                exitTransition = {
                    if (targetState.destination.route == "map" || targetState.destination.route == "admin_map") {
                        ExitTransition.None
                    } else {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(animDuration, easing = animEasing)
                        ) + fadeOut(animationSpec = tween(animDuration))
                    }
                },
                popEnterTransition = {
                    if (initialState.destination.route == "map" || initialState.destination.route == "admin_map") {
                        EnterTransition.None
                    } else {
                        fadeIn(animationSpec = tween(animDuration))
                    }
                },
                popExitTransition = {
                    if (targetState.destination.route == "map" || targetState.destination.route == "admin_map") {
                        ExitTransition.None
                    } else {
                        fadeOut(animationSpec = tween(animDuration))
                    }
                }
            ) {
                Mapview(
                    navController = navController,
                    isAdminMode = isAdminMode,
                    isEditorMode = isEditorMode,
                    onEditorModeChange = { isEditorMode = it }
                )
            }

            composable(
                "admin_map",
                enterTransition = {
                    if (initialState.destination.route == "bookmarks" || initialState.destination.route == "map") {
                        EnterTransition.None
                    } else if (initialState.destination.route == "datatable") {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(animDuration, easing = animEasing)
                        ) + fadeIn(animationSpec = tween(animDuration))
                    } else {
                        fadeIn(animationSpec = tween(animDuration))
                    }
                },
                exitTransition = {
                    if (targetState.destination.route == "bookmarks" || targetState.destination.route == "map") {
                        ExitTransition.None
                    } else if (targetState.destination.route == "datatable") {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(animDuration, easing = animEasing)
                        ) + fadeOut(animationSpec = tween(animDuration))
                    } else {
                        fadeOut(animationSpec = tween(animDuration))
                    }
                },
                popEnterTransition = {
                    if (initialState.destination.route == "bookmarks" || initialState.destination.route == "map") {
                        EnterTransition.None
                    } else {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(animDuration, easing = animEasing)
                        ) + fadeIn(animationSpec = tween(animDuration))
                    }
                },
                popExitTransition = {
                    if (targetState.destination.route == "bookmarks" || targetState.destination.route == "map") {
                        ExitTransition.None
                    } else {
                        fadeOut(animationSpec = tween(animDuration))
                    }
                }
            ) {
                Mapview(
                    navController = navController,
                    isAdminMode = true,
                    isEditorMode = isEditorMode,
                    onEditorModeChange = { isEditorMode = it }
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
