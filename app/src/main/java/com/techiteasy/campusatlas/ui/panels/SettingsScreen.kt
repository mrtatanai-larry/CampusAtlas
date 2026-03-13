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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.techiteasy.campusatlas.R
import com.techiteasy.campusatlas.ui.theme.CampusAtlasTheme
import kotlinx.coroutines.delay
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()
    
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

    val backgroundColor = if (isDark) Color.Black else Color(0xFFFBFBFF)
    val cardColor = if (isDark) Color(0xFF1C1C1E) else Color(0xFFE5E5EA)
    val textColor = if (isDark) Color.White else Color(0xFF1C1C1E)
    val secondaryTextColor = if (isDark) Color.Gray else Color(0xFF8E8E93)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 17.dp)
        ) {
            // Updated top padding
            Spacer(modifier = Modifier.height(40.dp))

            // Circular Back Button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(if (isDark) Color(0xFF2C2C2E) else Color(0xFFE5E5EA), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = textColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Settings",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                letterSpacing = (-1).sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // App Mode Card Section
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("App Mode", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textColor)
                        Text("Current Mode: User", fontSize = 12.sp, color = secondaryTextColor)
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Switch to Admin Button using drawable icons
                        Surface(
                            onClick = { /* Admin Click Logic */ },
                            modifier = Modifier.weight(1.1f).height(64.dp),
                            shape = RoundedCornerShape(20.dp),
                            color = if (isDark) Color(0xFF2C2C2E) else Color(0xFFD1D1D6)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            ) {
                                Surface(modifier = Modifier.size(34.dp), shape = CircleShape, color = Color(0xFFF2D398)) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.compare_arrows_24px),
                                        contentDescription = null,
                                        modifier = Modifier.padding(7.dp),
                                        tint = Color.Black
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Switch to Admin", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = textColor)
                            }
                        }

                        // School Code TextField
                        TextField(
                            value = "",
                            onValueChange = {},
                            placeholder = { Text("Enter School Code.....", fontSize = 11.sp, color = secondaryTextColor) },
                            modifier = Modifier.weight(0.9f).height(64.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = if (isDark) Color(0xFF2C2C2E) else Color(0xFFD1D1D6),
                                unfocusedContainerColor = if (isDark) Color(0xFF2C2C2E) else Color(0xFFD1D1D6),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = textColor
                            ),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Reset Button
                    Surface(
                        onClick = { /* Reset Click Logic */ },
                        modifier = Modifier.fillMaxWidth().height(64.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = if (isDark) Color(0xFF2C2C2E) else Color(0xFFD1D1D6)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            Surface(modifier = Modifier.size(34.dp), shape = CircleShape, color = Color(0xFFA8F2A8)) {
                                Icon(
                                    painter = painterResource(id = R.drawable.restart_alt_24px),
                                    contentDescription = null,
                                    modifier = Modifier.padding(7.dp),
                                    tint = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Reset app setup", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = textColor)
                        }
                    }
                }
            }

            // Map Data Section
            SettingsSectionTitle("Map Data", textColor)
            SettingsPillItem(
                painter = painterResource(id = R.drawable.restart_alt_24px),
                iconBg = Color(0xFFA8A8F2),
                text = "Refresh Map Data",
                textColor = textColor,
                cardColor = cardColor,
                onClick = { /* Logic for Refresh Map Data */ }
            )
            Spacer(modifier = Modifier.height(10.dp))
            SettingsPillItem(
                painter = painterResource(id = R.drawable.cached_24px),
                iconBg = Color(0xFFF2A8A8),
                text = "Clear App Cache",
                textColor = textColor,
                cardColor = cardColor,
                onClick = { 
                    try {
                        val size = getFolderSize(context.cacheDir)
                        val formattedSize = Formatter.formatFileSize(context, size)
                        context.cacheDir.deleteRecursively()
                        snackbarMessage = "Cache removed $formattedSize"
                    } catch (_: Exception) {
                        // Cache clear failed silently
                    }
                }
            )

            // About Section
            SettingsSectionTitle("About", textColor)
            SettingsPillItem(
                painter = painterResource(id = R.drawable.info_24px),
                iconBg = Color(0xFFA8E5F2),
                text = "Capstone Project Created by PCC Students",
                textColor = textColor,
                cardColor = cardColor
            )
            Spacer(modifier = Modifier.height(10.dp))
            SettingsPillItem(
                painter = painterResource(id = R.drawable.deployed_code_24px),
                iconBg = Color(0xFFC7C7CC),
                text = "version: $appVersion",
                textColor = textColor,
                cardColor = cardColor
            )

            // Developer Section
            SettingsSectionTitle("Developer", textColor)
            SettingsPillItem(
                painter = painterResource(id = R.drawable.deployed_code_24px),
                iconBg = Color(0xFFC7C7CC),
                text = "Renmar O. Dumol",
                textColor = textColor,
                cardColor = cardColor
            )
            Spacer(modifier = Modifier.height(10.dp))
            SettingsPillItem(
                painter = painterResource(id = R.drawable.deployed_code_24px),
                iconBg = Color(0xFFC7C7CC),
                text = "Sophia May P. Palomo",
                textColor = textColor,
                cardColor = cardColor
            )

            // Members Section
            SettingsSectionTitle("Members", textColor)
            SettingsPillItem(
                painter = painterResource(id = R.drawable.deployed_code_24px),
                iconBg = Color(0xFFC7C7CC),
                text = "Jb L. Joguilon",
                textColor = textColor,
                cardColor = cardColor
            )
            Spacer(modifier = Modifier.height(10.dp))
            SettingsPillItem(
                painter = painterResource(id = R.drawable.deployed_code_24px),
                iconBg = Color(0xFFC7C7CC),
                text = "Ian Charles E. Sorsan",
                textColor = textColor,
                cardColor = cardColor
            )
            Spacer(modifier = Modifier.height(10.dp))
            SettingsPillItem(
                painter = painterResource(id = R.drawable.deployed_code_24px),
                iconBg = Color(0xFFC7C7CC),
                text = "Aizza P. Panizales",
                textColor = textColor,
                cardColor = cardColor
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
                color = if (isDark) Color(0xFF333333) else Color(0xFF323232),
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 6.dp,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = snackbarMessage ?: "",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String, color: Color) {
    Spacer(modifier = Modifier.height(28.dp))
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = Modifier.padding(start = 4.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun SettingsPillItem(
    painter: Painter,
    iconBg: Color,
    text: String,
    textColor: Color,
    cardColor: Color,
    onClick: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(72.dp),
        shape = RoundedCornerShape(36.dp),
        color = cardColor,
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
                Icon(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.padding(14.dp),
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
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

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun SettingsScreenLightPreview() {
    CampusAtlasTheme(darkTheme = false) {
        SettingsScreen(onBackClick = {})
    }
}
