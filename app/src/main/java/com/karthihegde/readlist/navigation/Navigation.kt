package com.karthihegde.readlist.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.karthihegde.readlist.database.BookViewModel
import com.karthihegde.readlist.navigation.screens.BookNavScreens
import com.karthihegde.readlist.navigation.screens.Screens
import com.karthihegde.readlist.ui.BookDetailView
import com.karthihegde.readlist.ui.CollectionScreen
import com.karthihegde.readlist.ui.DiscoverScreen
import com.karthihegde.readlist.ui.ProgressView
import com.karthihegde.readlist.utils.clickBook
import com.karthihegde.readlist.utils.getBookFromId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun Navigation() {
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = Screens.Discover.route) {
        composable(route = Screens.Discover.route) {
            DiscoverScreen(navHostController)
        }
        composable(route = BookNavScreens.DetailView.route + "/{item}", arguments = listOf(
            navArgument("item") {
                type = NavType.StringType
            }
        )) { args ->
            args.arguments?.getString("item")?.let {
                val scope = CoroutineScope(Job() + Dispatchers.IO)
                scope.launch {
                    clickBook.value = getBookFromId(it)
                }
                BookDetailView(navHostController = navHostController)
            }
        }
        composable(route = Screens.Collection.route) {
            val viewModel = BookViewModel(LocalContext.current.applicationContext as Application)
            CollectionScreen(viewModel, navHostController)
        }
        composable(route = Screens.Progress.route) {
            ProgressView(navController = navHostController)
        }
    }

}

