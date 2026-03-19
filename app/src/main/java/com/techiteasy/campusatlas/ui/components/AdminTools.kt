package com.techiteasy.campusatlas.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techiteasy.campusatlas.R
import com.techiteasy.campusatlas.ui.theme.CampusAtlasTheme

@Composable
fun AdminTools(
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // Editor Mode Badge
        Surface(
            modifier = Modifier
                .padding(top = 16.dp),
            shape = RoundedCornerShape(32.dp),
            color = colorScheme.surfaceContainerHigh,
            tonalElevation = 6.dp,
            shadowElevation = 6.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape,
                    color = colorScheme.primaryContainer
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.compare_arrows_24px),
                        contentDescription = null,
                        modifier = Modifier.padding(6.dp),
                        tint = colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Editor Mode",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Normal,
                    color = colorScheme.onSurface
                )
            }
        }

        // Side Buttons (Settings & Add Location)
        Column(
            modifier = Modifier.padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FloatingActionButton(
                onClick = onSettingsClick,
                containerColor = colorScheme.surface,
                contentColor = colorScheme.primary,
                shape = CircleShape,
                modifier = Modifier.size(56.dp),
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = "Settings",
                    modifier = Modifier.size(24.dp)
                )
            }

            FloatingActionButton(
                onClick = { /* TODO */ },
                containerColor = colorScheme.surface,
                contentColor = colorScheme.primary,
                shape = CircleShape,
                modifier = Modifier.size(56.dp),
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_location_alt_24px),
                    contentDescription = "Add Location",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminToolsPreview() {
    CampusAtlasTheme {
        AdminTools(onSettingsClick = {})
    }
}
