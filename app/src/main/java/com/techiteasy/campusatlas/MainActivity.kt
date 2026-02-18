package com.techiteasy.campusatlas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.techiteasy.campusatlas.ui.navigation.AppNav
import com.techiteasy.campusatlas.ui.theme.CampusAtlasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CampusAtlasTheme {
                AppNav()
            }
        }
    }
}
