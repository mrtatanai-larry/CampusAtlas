package com.techiteasy.campusatlas.ui.mainscreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.techiteasy.campusatlas.R
import com.techiteasy.campusatlas.ui.components.AdminTools
import com.techiteasy.campusatlas.ui.components.NavButtons
import com.techiteasy.campusatlas.ui.theme.CampusAtlasTheme

@Composable
fun AdminMapOverlay(
    navController: NavController,
    onRunSetupClick: () -> Unit,
    modifier: Modifier = Modifier,
    showRunSetup: Boolean = true
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Box(modifier = modifier.fillMaxSize()) {
        AdminTools(
            onSettingsClick = {
                navController.navigate("settings") { launchSingleTop = true }
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp)
        )
        
        // Run Setup Button - Positioned at the bottom end, only shown if showRunSetup is true
        if (showRunSetup) {
            ExtendedFloatingActionButton(
                onClick = onRunSetupClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 124.dp, end = 24.dp),
                containerColor = colorScheme.secondaryContainer,
                contentColor = colorScheme.onSecondaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                shape = RoundedCornerShape(24.dp),
            ) {
                Text(
                    text = "Run Setup", 
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(id = R.drawable.build_circle_24px),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
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
                    onRunSetupClick = {},
                    showRunSetup = true
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
                    onRunSetupClick = {},
                    showRunSetup = true
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
