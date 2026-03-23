package com.techiteasy.campusatlas.ui.panels

import android.content.pm.PackageManager
import android.os.Build
import android.text.format.Formatter
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.techiteasy.campusatlas.R
import com.techiteasy.campusatlas.ui.theme.CampusAtlasTheme
import kotlinx.coroutines.delay
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    isAdminMode: Boolean = false,
    canSwitchAdminMode: Boolean = false,
    onAdminModeChange: (Boolean) -> Unit = {},
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme
    
    // State for floating message
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    
    // Auto-hide message after delay
    LaunchedEffect(snackbarMessage) {
        if (snackbarMessage != null) {
            delay(3000)
            snackbarMessage = null
        }
    }

    // Get app version dynamically
    val appVersion = remember {
        try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
            packageInfo.versionName ?: "Unknown"
        } catch (_: Exception) {
            "1.0"
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(48.dp)
                        .background(colorScheme.surfaceVariant, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }

                if (isAdminMode) {
                    Icon(
                        painter = painterResource(id = R.drawable.account_circle_24px),
                        contentDescription = "Account",
                        tint = colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (isAdminMode || canSwitchAdminMode) {
                Text(
                    text = "App Mode",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                SettingsPillItem(
                    painter = painterResource(id = R.drawable.compare_arrows_24px),
                    iconBg = Color(0xFFF0E4B8),
                    iconTint = Color(0xFF8B7500),
                    text = if (isAdminMode) "Switch to User" else "Switch to Admin",
                    onClick = { onAdminModeChange(!isAdminMode) }
                )
            }

            if (isAdminMode) {
                SettingsSectionTitle("Tools")
                
                SettingsPillItem(
                    icon = Icons.Default.HelpOutline,
                    iconBg = Color(0xFFC4D3FF),
                    iconTint = Color(0xFF00227B),
                    text = "Help",
                    onClick = { /* Logic for Help */ }
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                SettingsPillItem(
                    painter = painterResource(id = R.drawable.cached_24px),
                    iconBg = Color(0xFFFFB3B3),
                    iconTint = Color(0xFFB3261E),
                    text = "Clear App Cache",
                    onClick = { 
                        try {
                            val size = getFolderSize(context.cacheDir)
                            val formattedSize = Formatter.formatFileSize(context, size)
                            context.cacheDir.deleteRecursively()
                            snackbarMessage = "Cache removed $formattedSize"
                        } catch (_: Exception) { }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                SettingsPillItem(
                    painter = painterResource(id = R.drawable.restart_alt_24px),
                    iconBg = Color(0xFFB3F5BC),
                    iconTint = Color(0xFF1B5E20),
                    text = "Reset App Setup",
                    onClick = {
                        navController.navigate("setup") {
                            popUpTo("settings") { inclusive = true }
                        }
                    }
                )
            } else {
                SettingsSectionTitle("App Data")
                
                SettingsPillItem(
                    painter = painterResource(id = R.drawable.restart_alt_24px),
                    iconBg = Color(0xFFB3F5BC),
                    iconTint = Color(0xFF1B5E20),
                    text = "Reset App Setup",
                    onClick = {
                        navController.navigate("setup") {
                            popUpTo("settings") { inclusive = true }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                SettingsPillItem(
                    painter = painterResource(id = R.drawable.cached_24px),
                    iconBg = Color(0xFFFFB3B3),
                    iconTint = Color(0xFFB3261E),
                    text = "Clear App Cache",
                    onClick = { 
                        try {
                            val size = getFolderSize(context.cacheDir)
                            val formattedSize = Formatter.formatFileSize(context, size)
                            context.cacheDir.deleteRecursively()
                            snackbarMessage = "Cache removed $formattedSize"
                        } catch (_: Exception) { }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                SettingsPillItem(
                    painter = painterResource(id = R.drawable.restart_alt_24px),
                    iconBg = Color(0xFFC4D3FF),
                    iconTint = Color(0xFF00227B),
                    text = "Refresh Map Data",
                    onClick = { /* Logic for Refresh Map Data */ }
                )
            }

            SettingsSectionTitle("About")
            SettingsPillItem(
                painter = painterResource(id = R.drawable.info_24px),
                iconBg = Color(0xFFAEEAFA),
                iconTint = Color(0xFF006875),
                text = "Capstone Project Created by PCC Students"
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingsPillItem(
                painter = painterResource(id = R.drawable.deployed_code_24px),
                iconBg = Color(0xFFD9D9D9),
                iconTint = Color(0xFF49454F),
                text = "version: $appVersion"
            )

            SettingsSectionTitle("Developer")
            SettingsPillItem(
                painter = painterResource(id = R.drawable.deployed_code_24px),
                iconBg = colorScheme.surfaceVariant,
                text = "Renmar O. Dumol"
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingsPillItem(
                painter = painterResource(id = R.drawable.deployed_code_24px),
                iconBg = colorScheme.surfaceVariant,
                text = "Sophia May P. Palomo"
            )

            SettingsSectionTitle("Members")
            SettingsPillItem(
                painter = painterResource(id = R.drawable.deployed_code_24px),
                iconBg = colorScheme.surfaceVariant,
                text = "Jb L. Joguilon"
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingsPillItem(
                painter = painterResource(id = R.drawable.deployed_code_24px),
                iconBg = colorScheme.surfaceVariant,
                text = "Ian Charles E. Sorsan"
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingsPillItem(
                painter = painterResource(id = R.drawable.deployed_code_24px),
                iconBg = colorScheme.surfaceVariant,
                text = "Aizza P. Panizales"
            )

            Spacer(modifier = Modifier.height(48.dp))
        }

        // Floating message overlay
        AnimatedVisibility(
            visible = snackbarMessage != null,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Surface(
                color = colorScheme.inverseSurface,
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 6.dp,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = snackbarMessage ?: "",
                    color = colorScheme.inverseOnSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Spacer(modifier = Modifier.height(28.dp))
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(start = 4.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun SettingsPillItem(
    painter: Painter? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    iconBg: Color,
    iconTint: Color? = null,
    text: String,
    onClick: (() -> Unit)? = null
) {
    val colorScheme = MaterialTheme.colorScheme
    val finalIconTint = iconTint ?: colorScheme.onSurfaceVariant
    
    Surface(
        modifier = Modifier.fillMaxWidth().height(72.dp),
        shape = RoundedCornerShape(36.dp),
        color = colorScheme.surfaceVariant.copy(alpha = 0.5f),
        onClick = onClick ?: {}
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(52.dp),
                shape = CircleShape,
                color = iconBg
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (painter != null) {
                        Icon(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = finalIconTint
                        )
                    } else if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = finalIconTint
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private fun getFolderSize(file: File?): Long {
    if (file == null || !file.exists()) return 0L
    if (!file.isDirectory) return file.length()
    var size = 0L
    val files = file.listFiles()
    if (files != null) {
        for (f in files) {
            size += getFolderSize(f)
        }
    }
    return size
}

@Preview(showBackground = true, name = "Light Mode Admin")
@Composable
fun SettingsScreenAdminPreview() {
    CampusAtlasTheme(darkTheme = false) {
        SettingsScreen(
            navController = rememberNavController(),
            isAdminMode = true,
            canSwitchAdminMode = true,
            onBackClick = {}
        )
    }
}
