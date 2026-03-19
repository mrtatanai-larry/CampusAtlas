package com.techiteasy.campusatlas.ui.panels

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
        bottomBar = {
            NavButtons(
                navController = navController,
                currentScreen = "datatable",
                isAdminMode = true,
                onMapClick = {
                    navController.navigate("admin_map") {
                        popUpTo("admin_map") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* TODO: Upload to Firebase */ },
                containerColor = colorScheme.primaryContainer,
                contentColor = colorScheme.onPrimaryContainer,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 9.dp, end = 3.dp)
            ) {
                Text("Upload to Firebase", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Spacer(Modifier.width(12.dp))
                Icon(
                    painter = painterResource(R.drawable.database_upload_24px),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        containerColor = colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Database",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground,
                letterSpacing = (-1).sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Generated Code Section
            Text(
                text = "Generated Code",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(72.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left part: Code display (Pill shape cut on the right)
                Surface(
                    modifier = Modifier.weight(1.8f).fillMaxHeight(),
                    shape = RoundedCornerShape(topStart = 42.dp, bottomStart = 42.dp, topEnd = 8.dp, bottomEnd = 8.dp),
                    color = colorScheme.surfaceVariant.copy(alpha = 0.7f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Generated Code Will Display here",
                            color = colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            fontSize = 8.sp
                        )
                        Icon(
                            painter = painterResource(R.drawable.copy_all_24px),
                            contentDescription = "Copy Code",
                            tint = colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(3.dp))
                
                // Right part: Regenerate Button Container (Pill shape cut on the left)
                Surface(
                    modifier = Modifier.weight(1.1f).fillMaxHeight(),
                    shape = RoundedCornerShape(topEnd = 42.dp, bottomEnd = 42.dp, topStart = 8.dp, bottomStart = 8.dp),
                    color = colorScheme.surfaceVariant.copy(alpha = 0.7f)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {
                        Surface(
                            onClick = { /* TODO */ },
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(32.dp),
                            color = colorScheme.surface
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    "Regenerate",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 8.sp,
                                    color = colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Upload Image Section
            Text(
                text = "Upload Image",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                modifier = Modifier.fillMaxWidth().height(72.dp),
                shape = RoundedCornerShape(42.dp),
                color = colorScheme.surfaceVariant.copy(alpha = 0.7f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Upload/Update Image here...",
                        color = colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        fontSize = 8.sp
                    )
                    Surface(
                        onClick = { /* TODO */ },
                        shape = CircleShape,
                        color = colorScheme.primaryContainer,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(R.drawable.upload_24px),
                                contentDescription = "Upload",
                                tint = colorScheme.onPrimaryContainer,
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
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // Empty space for cards as requested
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

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun DataTableScreenDarkPreview() {
    CampusAtlasTheme(darkTheme = true) {
        DataTableScreen(navController = rememberNavController())
    }
}
