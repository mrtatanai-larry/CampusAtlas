package com.techiteasy.campusatlas.ui.setup

import android.Manifest
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.techiteasy.campusatlas.ui.theme.CampusAtlasTheme
import kotlin.random.Random

@Composable
fun FirstSetupScreen(
    onFinish: (isAdminSelected: Boolean) -> Unit
) {
    val context = LocalContext.current
    var currentStep by remember { mutableIntStateOf(0) }
    var userMode by remember { mutableStateOf("User") }
    var schoolCode by remember { mutableStateOf("") }
    var adminPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }

    // Admin Specific State
    var isAdminLoggedIn by remember { mutableStateOf(false) }
    var adminGeneratedCode by remember { mutableStateOf("") }
    
    // Handle back gesture
    BackHandler(enabled = currentStep > 0) {
        currentStep--
    }

    // Check permission state for step 1
    var isLocationPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Validation logic for the "Next" button
    val canProceed = when (currentStep) {
        0 -> true
        1 -> isLocationPermissionGranted
        2 -> if (userMode == "User") {
            schoolCode.isNotBlank()
        } else {
            // Button will be highlighted even if password is wrong
            adminPassword.isNotBlank() && isAdminLoggedIn && adminGeneratedCode.isNotBlank()
        }
        else -> true
    }
    
    val colorScheme = MaterialTheme.colorScheme
    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        containerColor = colorScheme.surfaceVariant,
        // Set contentWindowInsets to 0 to handle edge-to-edge manually
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                color = colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .padding(horizontal = 32.dp, vertical = 24.dp)
                        .fillMaxWidth(),
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

                    FilledIconButton(
                        onClick = {
                            if (currentStep == 2 && userMode == "Admin") {
                                if (adminPassword == "Pnhs") {
                                    passwordError = false
                                    currentStep++
                                } else {
                                    passwordError = true
                                }
                            } else if (currentStep < 3) {
                                currentStep++
                            } else {
                                onFinish(userMode == "Admin")
                            }
                        },
                        enabled = canProceed,
                        modifier = Modifier.size(56.dp),
                        shape = CircleShape,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = colorScheme.primary,
                            contentColor = colorScheme.onPrimary,
                            disabledContainerColor = colorScheme.onSurface.copy(alpha = 0.12f),
                            disabledContentColor = colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    ) {
                        Icon(
                            imageVector = if (currentStep < 3) Icons.AutoMirrored.Filled.ArrowForward else Icons.Default.Check,
                            contentDescription = "Next",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = innerPadding.calculateStartPadding(layoutDirection),
                    end = innerPadding.calculateEndPadding(layoutDirection),
                    bottom = innerPadding.calculateBottomPadding()
                )
                .statusBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(top = 20.dp, bottom = 48.dp) // Increase top Spacing
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "StepTransition"
                ) { step ->
                    when (step) {
                        0 -> WelcomeStep()
                        1 -> LocationPermissionStep(
                            isGranted = isLocationPermissionGranted,
                            onPermissionChanged = { isLocationPermissionGranted = it }
                        )
                        2 -> UserSelectionStep(
                            userMode = userMode,
                            onModeChange = { userMode = it },
                            schoolCode = schoolCode,
                            onCodeChange = { schoolCode = it },
                            adminPassword = adminPassword,
                            onPasswordChange = { 
                                adminPassword = it
                                passwordError = false // Clear error when typing
                            },
                            passwordError = passwordError,
                            isAdminLoggedIn = isAdminLoggedIn,
                            onAdminLoginToggle = { loggedIn ->
                                isAdminLoggedIn = loggedIn
                                if (loggedIn) {
                                    // Automatically generate a code when logged in
                                    adminGeneratedCode = "PNHS-${Random.nextInt(1000, 9999)}"
                                } else {
                                    adminGeneratedCode = ""
                                }
                            },
                            generatedCode = adminGeneratedCode
                        )
                        3 -> SetupCompleteStep(
                            userMode = userMode
                        )
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
fun LocationPermissionStep(
    isGranted: Boolean,
    onPermissionChanged: (Boolean) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        onPermissionChanged(granted)
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
                if (!isGranted) {
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isGranted) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(20.dp),
            enabled = !isGranted
        ) {
            Text(
                if (isGranted) "Permission Granted" else "Grant Location Permission",
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
    onCodeChange: (String) -> Unit,
    adminPassword: String,
    onPasswordChange: (String) -> Unit,
    passwordError: Boolean,
    isAdminLoggedIn: Boolean,
    onAdminLoginToggle: (Boolean) -> Unit,
    generatedCode: String
) {
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
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
            // User UI
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
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = colorScheme.surface,
                    focusedContainerColor = colorScheme.surface,
                    unfocusedBorderColor = colorScheme.outline,
                    focusedBorderColor = colorScheme.primary
                ),
                trailingIcon = {
                    Surface(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = clipboard.primaryClip
                            if (clip != null && clip.itemCount > 0) {
                                val text = clip.getItemAt(0).text.toString()
                                onCodeChange(text)
                            }
                        },
                        modifier = Modifier.padding(end = 4.dp).size(36.dp),
                        shape = CircleShape,
                        color = colorScheme.surfaceVariant
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(id = com.techiteasy.campusatlas.R.drawable.content_paste_24px),
                                contentDescription = "Paste",
                                modifier = Modifier.size(20.dp),
                                tint = colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )
        } else {
            // Admin UI
            Text(
                text = "Manage campus map data. First sign in to generate your campus code.",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Admin Password Field
            Text("App Admin Password", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = adminPassword,
                onValueChange = onPasswordChange,
                placeholder = { Text("Put Required Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                singleLine = true,
                isError = passwordError,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    focusedContainerColor = colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = colorScheme.primary.copy(alpha = 0.5f),
                    errorContainerColor = colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    errorBorderColor = colorScheme.error
                ),
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        modifier = Modifier.alpha(0.8f) // 80 percent translucent
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = if (passwordError) colorScheme.error else colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
            
            if (passwordError) {
                Text(
                    text = "Password is wrong",
                    color = colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            // 1. Admin Account
            Text("Admin Account", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                onClick = { 
                    // Simulate Firebase login success
                    onAdminLoginToggle(!isAdminLoggedIn) 
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(20.dp),
                color = if (isAdminLoggedIn) colorScheme.primaryContainer else colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painterResource(id = com.techiteasy.campusatlas.R.drawable.google), 
                        contentDescription = null, 
                        modifier = Modifier.size(24.dp), 
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        if (isAdminLoggedIn) "Logged in with Google" else "Sign in with Google", 
                        style = MaterialTheme.typography.bodyLarge, 
                        fontWeight = FontWeight.Medium,
                        color = if (isAdminLoggedIn) colorScheme.onPrimaryContainer else colorScheme.onSurface
                    )
                    if (isAdminLoggedIn) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.Check, contentDescription = null, tint = colorScheme.primary, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Generated Code
            Text("Generated Campus Code", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = generatedCode,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Connect account to generate code") },
                modifier = Modifier.fillMaxWidth().alpha(if (isAdminLoggedIn) 1f else 0.5f),
                shape = RoundedCornerShape(20.dp),
                enabled = isAdminLoggedIn,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = colorScheme.surface,
                    focusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface.copy(alpha = 0.5f)
                ),
                trailingIcon = {
                    if (isAdminLoggedIn && generatedCode.isNotBlank()) {
                        Surface(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = android.content.ClipData.newPlainText("Campus Code", generatedCode)
                                clipboard.setPrimaryClip(clip)
                            },
                            modifier = Modifier.padding(end = 4.dp).size(36.dp),
                            shape = CircleShape,
                            color = colorScheme.surfaceVariant // Greyed out a bit as requested
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    painter = painterResource(id = com.techiteasy.campusatlas.R.drawable.copy_all_24px),
                                    contentDescription = "Copy",
                                    modifier = Modifier.size(20.dp),
                                    tint = colorScheme.onSurfaceVariant // Greyed out a bit as requested
                                )
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
            
            // Add navigation bar padding at the end of scrollable content
            Spacer(modifier = Modifier.navigationBarsPadding())
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
fun SetupCompleteStep(
    userMode: String
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Setup Complete",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (userMode == "User") 
                "You're ready to explore the campus." 
            else 
                "You now have access to manage campus map data and locations.",
            style = MaterialTheme.typography.bodyLarge,
            color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
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
