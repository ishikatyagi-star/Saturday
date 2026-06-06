package com.hackathon.saturday.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hackathon.saturday.ui.today.TodayScreen
import com.hackathon.saturday.ui.capture.CaptureScreen
import com.hackathon.saturday.ui.ask.AskScreen
import com.hackathon.saturday.ui.theme.AccentCyan
import com.hackathon.saturday.ui.theme.DeepSpace
import com.hackathon.saturday.ui.theme.SurfaceDark
import com.hackathon.saturday.ui.theme.TextSecondary

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val items = listOf(BottomNavItem.Today, BottomNavItem.Capture, BottomNavItem.Ask)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceDark,
                tonalElevation = 0.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentCyan,
                            selectedTextColor = AccentCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = DeepSpace
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Today.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Today.route) { TodayScreen() }
            composable(BottomNavItem.Capture.route) { CaptureScreen() }
            composable(BottomNavItem.Ask.route) { AskScreen() }
        }
    }
}
