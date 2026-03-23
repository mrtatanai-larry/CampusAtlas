package com.techiteasy.campusatlas.ui.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.techiteasy.campusatlas.ui.components.NavButtons
import com.techiteasy.campusatlas.ui.components.Searchbar
import com.techiteasy.campusatlas.ui.panels.BookmarksScreen
import com.techiteasy.campusatlas.ui.theme.CampusAtlasTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mapview(
    navController: NavController,
    isAdminMode: Boolean = false
) {
    val colorScheme = MaterialTheme.colorScheme
    var searchText by remember { mutableStateOf("") }
    var showBookmarks by remember { mutableStateOf(false) }
    var currentScreen by remember { mutableStateOf("map") }
    
    // Logic to check if there is map or image content
    // For now, we'll use a local state. You can connect this to your ViewModel later.
    var hasMapContent by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetScaffoldState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        BottomSheetScaffold(
            scaffoldState = sheetState,
            sheetPeekHeight = 0.dp,
            sheetDragHandle = { if (showBookmarks) DragHandle() },
            sheetContainerColor = colorScheme.surface,
            sheetContent = {
                if (showBookmarks) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.92f)
                            .padding(bottom = 80.dp)
                    ) {
                        BookmarksScreen()
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (!hasMapContent) {
                        Text(
                            text = if (isAdminMode) "Admin Map View" else "Campus Map Will Display Here",
                            style = MaterialTheme.typography.headlineMedium,
                            color = colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    } else {
                        // This is where your Map/Image content will be displayed
                        Text("Map Content Loaded")
                    }
                }

                if (isAdminMode) {
                    AdminMapOverlay(
                        navController = navController
                    )
                } else {
                    Searchbar(
                        searchText = searchText,
                        onSearchChanged = { searchText = it },
                        onSettingsClick = {
                            navController.navigate("settings") { launchSingleTop = true }
                        },
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 48.dp)
                    )
                }
            }
        }
        
        NavButtons(
            navController = navController,
            currentScreen = currentScreen,
            isAdminMode = isAdminMode,
            onBookmarksClick = {
                showBookmarks = true
                currentScreen = "bookmarks"
                scope.launch { sheetState.bottomSheetState.expand() }
            },
            onMapClick = {
                currentScreen = "map"
                scope.launch {
                    sheetState.bottomSheetState.partialExpand()
                    showBookmarks = false
                }
            },
            onDataTableClick = {
                currentScreen = "datatable"
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MapviewPreview() {
    CampusAtlasTheme {
        val navController = rememberNavController()
        Mapview(navController = navController)
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MapviewDarkPreview() {
    CampusAtlasTheme {
        val navController = rememberNavController()
        Mapview(navController = navController, isAdminMode = true)
    }
}
