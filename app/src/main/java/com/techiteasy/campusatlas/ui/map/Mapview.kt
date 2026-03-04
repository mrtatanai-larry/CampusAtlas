package com.techiteasy.campusatlas.ui.map

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun Mapview(navController: NavController) {

    var searchText by remember { mutableStateOf("") }
    var showBookmarks by remember { mutableStateOf(false) }

    var currentScreen by remember { mutableStateOf("map") }

    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetScaffoldState()

    val navHeight = 64.dp

    Box(modifier = Modifier.fillMaxSize()) {

        BottomSheetScaffold(
            scaffoldState = sheetState,
            sheetPeekHeight = 0.dp,
            sheetDragHandle = { if (showBookmarks) DragHandle() },
            sheetContent = {
                if (showBookmarks) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.9f)
                            .padding(bottom = navHeight)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
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
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                Searchbar(
                    searchText = searchText,
                    onSearchChanged = { searchText = it },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 55.dp)
                )
            }
        }

        NavButtons(
            navController = navController,
            currentScreen = currentScreen,
            onBookmarksClick = {
                showBookmarks = true
                currentScreen = "bookmarks" // <- update highlight
                scope.launch { sheetState.bottomSheetState.expand() }
            },
            onMapClick = {
                showBookmarks = false
                currentScreen = "map"
            },
            onSettingsClick = {
                showBookmarks = false
                currentScreen = "settings"
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