package com.techiteasy.campusatlas.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun Mapview(navController: NavController) {

    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) Color.Black else Color.White
    val surfaceColor = if (isDark) Color(0xFF121212) else Color.White
    val onSurfaceColor = if (isDark) Color.White else Color.Black

    var searchText by remember { mutableStateOf("") }
    var showBookmarks by remember { mutableStateOf(false) }
    var currentScreen by remember { mutableStateOf("map") }

    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetScaffoldState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        BottomSheetScaffold(
            scaffoldState = sheetState,
            sheetPeekHeight = 0.dp,
            sheetDragHandle = { if (showBookmarks) DragHandle() },
            sheetContainerColor = surfaceColor,
            sheetContent = {
                if (showBookmarks) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.92f)
                            .padding(bottom = 80.dp)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            item { BookmarksScreen() }
                        }
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
                    Text(
                        text = "Campus Map Will Display Here",
                        style = MaterialTheme.typography.headlineMedium,
                        color = onSurfaceColor.copy(alpha = 0.5f)
                    )
                }

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
        
        NavButtons(
            navController = navController,
            currentScreen = currentScreen,
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
