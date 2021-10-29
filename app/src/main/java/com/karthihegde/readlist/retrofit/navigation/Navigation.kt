package com.karthihegde.readlist.retrofit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.karthihegde.readlist.DiscoverScreen

@Composable
fun Navigation() {
    val navhostcontroller = rememberNavController()
    NavHost(navController = navhostcontroller, startDestination = Screens.Discover.route) {
        composable(route = Screens.Discover.route) {
            DiscoverScreen(navhostcontroller)
        }
        //TODO: Add other screenss
    }

}

