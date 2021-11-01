package com.karthihegde.readlist.retrofit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.karthihegde.readlist.retrofit.getBookFromId
import com.karthihegde.readlist.retrofit.navigation.screens.BookNavScreens
import com.karthihegde.readlist.retrofit.navigation.screens.Screens
import com.karthihegde.readlist.ui.BookDetailView
import com.karthihegde.readlist.ui.DiscoverScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun Navigation() {
    val navhostcontroller = rememberNavController()
    NavHost(navController = navhostcontroller, startDestination = Screens.Discover.route) {
        composable(route = Screens.Discover.route) {
            DiscoverScreen(navhostcontroller)
        }
        composable(route = BookNavScreens.DetailView.route + "/{item}", arguments = listOf(
            navArgument("item") {
                type = NavType.StringType
            }
        )) { args ->
            args.arguments?.getString("item")?.let {
                val scope = CoroutineScope(Job() + Dispatchers.IO)
                scope.launch {
                    getBookFromId(it)
                }
                BookDetailView(navHostController = navhostcontroller)
            }
        }
    }

}

