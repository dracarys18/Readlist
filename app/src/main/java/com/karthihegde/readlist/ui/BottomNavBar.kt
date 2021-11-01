package com.karthihegde.readlist.ui

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.karthihegde.readlist.retrofit.navigation.screens.Screens

@Composable
fun BottomBar(navHostController: NavController, barlist: List<Screens>) {
    BottomAppBar {
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        BottomNavigation(
            backgroundColor = MaterialTheme.colors.background,
        ) {
            barlist.forEach {
                BottomNavigationItem(
                    icon = { Icon(it.icon, contentDescription = "") },
                    selected = currentRoute == it.route,
                    label = { Text(text = it.label) },
                    onClick = { /*TODO*/ },
                    alwaysShowLabel = false
                )
            }
        }

    }
}
