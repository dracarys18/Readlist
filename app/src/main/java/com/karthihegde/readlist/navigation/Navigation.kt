package com.karthihegde.readlist.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.karthihegde.readlist.database.BookViewModel
import com.karthihegde.readlist.database.BookViewModelFactory
import com.karthihegde.readlist.navigation.screens.BookNavScreens
import com.karthihegde.readlist.navigation.screens.GeneralScreens
import com.karthihegde.readlist.navigation.screens.Screens
import com.karthihegde.readlist.ui.*
import com.karthihegde.readlist.ui.stats.StatScreen
import com.karthihegde.readlist.utils.clickBook
import com.karthihegde.readlist.utils.getBookFromId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Main Navigation Composable
 */
@ExperimentalComposeUiApi
@Composable
fun Navigation() {
    val navHostController = rememberNavController()
    val application = LocalContext.current.applicationContext as Application
    val viewModel: BookViewModel = viewModel(
        factory = BookViewModelFactory(application = application)
    )
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
                BookDetailView(viewModel = viewModel, navHostController = navHostController)
            }
        }
        composable(
            route = BookNavScreens.EditView.route + "/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { ids ->
            ids.arguments?.getString("id")?.let { id ->
                EditBookProgress(viewModel = viewModel, navController = navHostController, id = id)
            }
        }
        composable(route = Screens.Collection.route) {
            CollectionScreen(viewModel = viewModel, navController = navHostController)
        }
        composable(route = Screens.Progress.route) {
            ProgressView(viewModel = viewModel, navController = navHostController)
        }
        composable(route = GeneralScreens.StatScreen.route) {
            StatScreen(viewModel = viewModel, navController = navHostController)
        }
    }

}

