package com.techiteasy.campusatlas.ui.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.techiteasy.campusatlas.ui.components.NavButtons
import com.techiteasy.campusatlas.ui.components.Searchbar

@Composable
fun MapPanel(
    navController: NavController
) {
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            NavButtons(
                navController = navController,
                currentScreen = "map"
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Map
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Campus Map Will Display Here",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            // Top Nav
            Searchbar(
                searchText = searchText,
                onSearchChanged = { searchText = it },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 35.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapPanelPreview() {
    val navController = rememberNavController()
    MapPanel(navController = navController)
}
