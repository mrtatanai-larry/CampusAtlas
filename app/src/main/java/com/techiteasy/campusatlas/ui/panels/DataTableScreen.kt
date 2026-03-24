package com.techiteasy.campusatlas.ui.panels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.techiteasy.campusatlas.R
import com.techiteasy.campusatlas.ui.components.NavButtons
import com.techiteasy.campusatlas.ui.theme.CampusAtlasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataTableScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* TODO: Upload to Firebase */ },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
            ) {
                Text("Upload to Firebase", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(Modifier.width(12.dp))
                Icon(
                    painter = painterResource(R.drawable.database_upload_24px),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        bottomBar = {
            NavButtons(
                navController = navController,
                currentScreen = "datatable",
                isAdminMode = true
                // We'll use default navigation logic for clicks
            )
        },
        containerColor = colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Database",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground,
                letterSpacing = (-1).sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Generated Code Section
            Text(
                text = "Generated Code",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(72.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.weight(1.8f).fillMaxHeight(),
                    shape = RoundedCornerShape(topStart = 36.dp, bottomStart = 36.dp, topEnd = 8.dp, bottomEnd = 8.dp),
                    color = colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Generated Code Will Display here",
                            color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Icon(
                            painter = painterResource(R.drawable.copy_all_24px),
                            contentDescription = "Copy Code",
                            tint = colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(4.dp))
                
                Surface(
                    modifier = Modifier.weight(1.1f).fillMaxHeight(),
                    shape = RoundedCornerShape(topEnd = 36.dp, bottomEnd = 36.dp, topStart = 8.dp, bottomStart = 8.dp),
                    color = colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(8.dp)) {
                        Surface(
                            onClick = { /* TODO */ },
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(24.dp),
                            color = colorScheme.primaryContainer
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    "Regenerate",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Upload Image Section
            Text(
                text = "Upload Image",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth().height(72.dp),
                shape = RoundedCornerShape(36.dp),
                color = colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Upload/Update Image here...",
                        color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Surface(
                        onClick = { /* TODO */ },
                        shape = CircleShape,
                        color = colorScheme.secondaryContainer,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(R.drawable.upload_24px),
                                contentDescription = "Upload",
                                tint = colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Mark Locations Section
            Text(
                text = "Mark Locations",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // Empty space for cards
            Spacer(modifier = Modifier.height(200.dp))
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun DataTableScreenLightPreview() {
    CampusAtlasTheme(darkTheme = false) {
        DataTableScreen(navController = rememberNavController())
    }
}
