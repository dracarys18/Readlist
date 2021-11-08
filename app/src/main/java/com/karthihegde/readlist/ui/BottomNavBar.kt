package com.karthihegde.readlist.ui

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.karthihegde.readlist.navigation.screens.Screens
import com.karthihegde.readlist.utils.NAVSCREENS

@Composable
fun BottomBar(navHostController: NavController) {
    BottomAppBar {
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        BottomNavigation(
            backgroundColor = MaterialTheme.colors.background,
        ) {
            NAVSCREENS.forEach { screen ->
                BottomNavigationItem(
                    icon = { Icon(screen.icon, contentDescription = "") },
                    selected = currentRoute == screen.route,
                    label = { Text(text = screen.label) },
                    onClick = {
                        navHostController.navigate(screen.route)
                    },
                    alwaysShowLabel = false
                )
            }
        }

    }
}
