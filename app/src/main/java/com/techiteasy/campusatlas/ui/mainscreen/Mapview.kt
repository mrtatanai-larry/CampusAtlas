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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.techiteasy.campusatlas.ui.components.Searchbar
import com.techiteasy.campusatlas.ui.panels.BookmarksScreen
import com.techiteasy.campusatlas.ui.theme.CampusAtlasTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mapview(
    navController: NavController,
    isAdminMode: Boolean = false,
    isEditorMode: Boolean = true,
    onEditorModeChange: (Boolean) -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    var searchText by remember { mutableStateOf("") }
    
    // Sync sheet visibility with the current navigation route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isBookmarksRoute = currentRoute == "bookmarks"
    
    // Logic to check if there is map or image content
    var hasMapContent by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    
    // Initialize sheet state based on whether we are starting on the bookmarks route
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = if (isBookmarksRoute) SheetValue.Expanded else SheetValue.PartiallyExpanded
        )
    )

    // Handle updates when the route changes (e.g. clicking bottom nav)
    LaunchedEffect(currentRoute) {
        if (isBookmarksRoute) {
            scope.launch { sheetState.bottomSheetState.expand() }
        } else if (currentRoute == "map" || currentRoute == "admin_map") {
            scope.launch { sheetState.bottomSheetState.partialExpand() }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        BottomSheetScaffold(
            scaffoldState = sheetState,
            sheetPeekHeight = 0.dp,
            sheetDragHandle = { if (isBookmarksRoute) DragHandle() },
            sheetContainerColor = colorScheme.surface,
            sheetContent = {
                if (isBookmarksRoute) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.92f)
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
                            text = if (isAdminMode) {
                                if (isEditorMode) "Admin Map Editor" else "Admin Map Test View"
                            } else "Campus Map Will Display Here",
                            style = MaterialTheme.typography.headlineMedium,
                            color = colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    } else {
                        Text("Map Content Loaded")
                    }
                }

                if (isAdminMode) {
                    Box(modifier = Modifier.statusBarsPadding()) {
                        AdminMapOverlay(
                            navController = navController,
                            isEditorMode = isEditorMode,
                            onEditorModeChange = onEditorModeChange
                        )
                    }
                } else {
                    Searchbar(
                        searchText = searchText,
                        onSearchChanged = { searchText = it },
                        onSettingsClick = {
                            navController.navigate("settings") { launchSingleTop = true }
                        },
                        modifier = Modifier
                            .statusBarsPadding()
                            .align(Alignment.TopCenter)
                            .padding(top = 16.dp)
                    )
                }
            }
        }
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
