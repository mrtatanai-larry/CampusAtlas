package com.techiteasy.campusatlas.ui.panels

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techiteasy.campusatlas.ui.theme.CampusAtlasTheme

@Composable
fun BookmarksScreen(
) {

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
                style = MaterialTheme.typography.titleLarge
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
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ListCard(
    title: String,
    showDivider: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "0 places",
            style = MaterialTheme.typography.bodySmall
        )

        if (showDivider) {
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
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
