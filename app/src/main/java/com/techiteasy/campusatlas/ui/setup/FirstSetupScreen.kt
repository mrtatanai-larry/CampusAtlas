package com.techiteasy.campusatlas.ui.setup

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.techiteasy.campusatlas.ui.theme.CampusAtlasTheme

@Composable
fun FirstSetupScreen(
    onFinish: () -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }
    var userMode by remember { mutableStateOf("User") } // "User" or "Admin"
    var schoolCode by remember { mutableStateOf("") }
    
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colorScheme.surfaceVariant
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Content Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 48.dp)
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "StepTransition"
                ) { step ->
                    when (step) {
                        0 -> WelcomeStep()
                        1 -> LocationPermissionStep(onPermissionGranted = { currentStep++ })
                        2 -> UserSelectionStep(
                            userMode = userMode,
                            onModeChange = { userMode = it },
                            schoolCode = schoolCode,
                            onCodeChange = { schoolCode = it }
                        )
                        3 -> SetupCompleteStep(userMode = userMode)
                    }
                }
            }

            // Bottom Navigation Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                color = colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (currentStep == 0) "Let's Go!" else "Step $currentStep",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface
                        )
                    }

                    if (currentStep != 1) { 
                        IconButton(
                            onClick = {
                                if (currentStep < 3) {
                                    currentStep++
                                } else {
                                    onFinish()
                                }
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .background(colorScheme.primaryContainer, CircleShape)
                        ) {
                            Icon(
                                imageVector = if (currentStep < 3) Icons.AutoMirrored.Filled.ArrowForward else Icons.Default.Check,
                                contentDescription = "Next",
                                tint = colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeStep() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Welcome to\nPNHS Campus Nav",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Let's first setup the app.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun LocationPermissionStep(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current
    var isPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        isPermissionGranted = granted
        if (granted) onPermissionGranted()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Location Permission",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Allow location access to show your position on the map and help you navigate around campus.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            lineHeight = 22.sp
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = {
                if (isPermissionGranted) {
                    onPermissionGranted()
                } else {
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                if (isPermissionGranted) "Permission Granted - Continue" else "Grant Location Permission",
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun UserSelectionStep(
    userMode: String,
    onModeChange: (String) -> Unit,
    schoolCode: String,
    onCodeChange: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "User Selection",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Choose your access level. User Mode is for navigation, Admin Mode requires login and allows map management.",
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            lineHeight = 18.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ModeButton(
                text = "User Mode",
                isSelected = userMode == "User",
                onClick = { onModeChange("User") },
                modifier = Modifier.weight(1f)
            )
            ModeButton(
                text = "Admin Mode",
                isSelected = userMode == "Admin",
                onClick = { onModeChange("Admin") },
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        if (userMode == "User") {
            Text(
                text = "Choose the campus map, search for locations, and save bookmarks. Works offline after setup.",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text("Enter Code", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = schoolCode,
                onValueChange = onCodeChange,
                placeholder = { Text("Enter School Provided Code") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = colorScheme.surface,
                    focusedContainerColor = colorScheme.surface,
                    unfocusedBorderColor = colorScheme.outline,
                    focusedBorderColor = colorScheme.primary
                )
            )
        } else {
            Text(
                text = "Manage campus map data, add or edit locations, and update information. Requires admin access.",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Text("Sign Up Your Google Account", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                onClick = { /* Handle Google Sign In */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(20.dp),
                color = colorScheme.surface,
                border = null,
                tonalElevation = 2.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_info_details),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Sign in with Google", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Upload Image", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Upload/Update Image here...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                trailingIcon = {
                    Surface(
                        modifier = Modifier.size(32.dp).padding(end = 4.dp),
                        shape = CircleShape,
                        color = colorScheme.secondaryContainer
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.padding(6.dp), tint = colorScheme.onSecondaryContainer)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = colorScheme.surface,
                    focusedContainerColor = colorScheme.surface,
                    unfocusedBorderColor = colorScheme.outline,
                    focusedBorderColor = colorScheme.primary
                )
            )
        }
    }
}

@Composable
fun ModeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) colorScheme.primary else colorScheme.secondaryContainer,
            contentColor = if (isSelected) colorScheme.onPrimary else colorScheme.onSecondaryContainer
        ),
        shape = RoundedCornerShape(24.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SetupCompleteStep(userMode: String) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Setup Complete",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (userMode == "User") 
                "You're ready to explore the campus." 
            else 
                "You now have access to manage campus map data and locations.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FirstSetupScreenPreview() {
    CampusAtlasTheme {
        FirstSetupScreen(onFinish = {})
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FirstSetupScreenDarkPreview() {
    CampusAtlasTheme {
        FirstSetupScreen(onFinish = {})
    }
}
