package com.hackathon.saturday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hackathon.saturday.ui.navigation.AppNavigation
import com.hackathon.saturday.ui.theme.SaturdayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SaturdayTheme {
                AppNavigation()
            }
        }
    }
}
