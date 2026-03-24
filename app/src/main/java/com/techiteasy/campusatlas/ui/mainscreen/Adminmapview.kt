package com.techiteasy.campusatlas.ui.mainscreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.techiteasy.campusatlas.ui.components.AdminTools
import com.techiteasy.campusatlas.ui.components.NavButtons
import com.techiteasy.campusatlas.ui.theme.CampusAtlasTheme

@Composable
fun AdminMapOverlay(
    navController: NavController,
    isEditorMode: Boolean,
    onEditorModeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        AdminTools(
            isEditorMode = isEditorMode,
            onModeChange = onEditorModeChange,
            onSettingsClick = {
                navController.navigate("settings") { launchSingleTop = true }
            },
            onAddLocationClick = { /* Handle add location */ },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AdminMapOverlayPreview() {
    CampusAtlasTheme {
        val navController = rememberNavController()
        Surface(modifier = Modifier.fillMaxSize()) {
            Box {
                // Background placeholder
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Map Background", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                }
                
                AdminMapOverlay(
                    navController = navController,
                    isEditorMode = true,
                    onEditorModeChange = {}
                )
                
                NavButtons(
                    navController = navController,
                    currentScreen = "map",
                    isAdminMode = true,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AdminMapOverlayDarkPreview() {
    CampusAtlasTheme {
        val navController = rememberNavController()
        Surface(modifier = Modifier.fillMaxSize()) {
            Box {
                // Background placeholder
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Map Background", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                }
                
                AdminMapOverlay(
                    navController = navController,
                    isEditorMode = true,
                    onEditorModeChange = {}
                )
                
                NavButtons(
                    navController = navController,
                    currentScreen = "map",
                    isAdminMode = true,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}
