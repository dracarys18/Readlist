package com.karthihegde.readlist.ui

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.karthihegde.readlist.utils.navScreens

/**
 * Composable Function which generates Navigation Bar
 *
 * @param navHostController Navigation Host Controller
 */
@Composable
fun BottomBar(navHostController: NavController) {
    CompositionLocalProvider(LocalElevationOverlay provides null) {
        BottomAppBar {
            val navBackStackEntry by navHostController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            BottomNavigation(
                backgroundColor = MaterialTheme.colors.background,
            ) {
                navScreens.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = "") },
                        selected = currentRoute == screen.route,
                        label = { Text(text = screen.label) },
                        onClick = {
                            navHostController.navigate(screen.route)
                        },
                        selectedContentColor = Color(0xff1976d2),
                        unselectedContentColor = MaterialTheme.colors.onBackground,
                        alwaysShowLabel = true
                    )
                }
            }

        }
    }
}
