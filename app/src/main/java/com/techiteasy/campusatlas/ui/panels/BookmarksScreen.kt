package com.techiteasy.campusatlas.ui.panels

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techiteasy.campusatlas.ui.theme.CampusAtlasTheme

@Composable
fun BookmarksScreen() {
    val textColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Your lists",
                style = MaterialTheme.typography.titleLarge,
                color = textColor
            )

            TextButton(
                onClick = { /* add new List function */ }
            ) {
                Text("+ New list")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ListCard(title = "Starred places")

        Spacer(modifier = Modifier.height(12.dp))

        ListCard(title = "Favorites")

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No saved places yet.",
            style = MaterialTheme.typography.bodyMedium,
            color = textColor.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ListCard(
    title: String,
    showDivider: Boolean = true
) {
    val textColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "0 places",
            style = MaterialTheme.typography.bodySmall,
            color = textColor.copy(alpha = 0.6f)
        )

        if (showDivider) {
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = textColor.copy(alpha = 0.12f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookmarksScreenPreview() {
    CampusAtlasTheme {
        BookmarksScreen()
    }
}
