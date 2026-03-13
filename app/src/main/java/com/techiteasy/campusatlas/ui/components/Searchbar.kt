package com.techiteasy.campusatlas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techiteasy.campusatlas.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Searchbar(
    searchText: String,
    onSearchChanged: (String) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()

    val containerColor = if (isDark) Color(0xFF1A1A1A) else Color.White
    val contentColor = if (isDark) Color.White else Color.Black
    val shadowColor = if (isDark) Color.Transparent else Color.Black.copy(alpha = 0.1f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextField(
            value = searchText,
            onValueChange = onSearchChanged,
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .shadow(
                    elevation = if (isDark) 0.dp else 8.dp, 
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = shadowColor,
                    spotColor = shadowColor
                ),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 15.sp,
                color = contentColor
            ),
            placeholder = { 
                Text(
                    "Search location...", 
                    fontSize = 15.sp,
                    color = contentColor.copy(alpha = 0.6f)
                ) 
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search",
                    modifier = Modifier.size(20.dp),
                    tint = contentColor.copy(alpha = 0.7f)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(28.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = contentColor
            )
        )

        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .size(56.dp)
                .shadow(
                    elevation = if (isDark) 0.dp else 8.dp, 
                    shape = CircleShape,
                    ambientColor = shadowColor,
                    spotColor = shadowColor
                )
                .background(containerColor, CircleShape)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = "Settings",
                modifier = Modifier.size(24.dp),
                tint = contentColor.copy(alpha = 0.8f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchbarPreview() {
    Searchbar(
        searchText = "",
        onSearchChanged = {},
        onSettingsClick = {}
    )
}
