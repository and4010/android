package com.example.survival.navigation

import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.survival.navigation.Screen.Companion.ScreenSaver
import com.example.survival.navigation.Screen.Companion.bottomNavItems
import com.example.survival.navigation.Screen.Home
import com.example.survival.navigation.Screen.Second
import com.example.survival.ui.home.HomeScreen
import com.example.survival.ui.second.SecondScreen




@Composable
fun NavManager() {
    val navController = rememberNavController()
    var currentDestination by rememberSaveable(stateSaver = ScreenSaver) {
        mutableStateOf(bottomNavItems.first())
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            bottomNavItems.forEach {
                item(
                    icon = { },
                    label = { Text(it.text()) },
                    selected = it == currentDestination,
                    onClick = {
                        currentDestination = it
                        navController.navigate(it.text()) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) {
        AppNavHost(navController)
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Home.text()
    ) {
        composable(Home.text()) {
            HomeScreen()
        }
        composable(Second.text()) {
            SecondScreen()
        }
    }
}