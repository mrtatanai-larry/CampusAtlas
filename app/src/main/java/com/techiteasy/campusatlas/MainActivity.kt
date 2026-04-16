package com.techiteasy.campusatlas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.techiteasy.campusatlas.ui.theme.CampusAtlasTheme

import android.content.Context
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize osmdroid configuration
        Configuration.getInstance().load(applicationContext, applicationContext.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))

        enableEdgeToEdge()

        setContent {
            CampusAtlasTheme {
                AppMain()
            }
        }
    }
}
