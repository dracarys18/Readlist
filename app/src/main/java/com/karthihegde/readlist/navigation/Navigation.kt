package com.karthihegde.readlist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.karthihegde.readlist.navigation.screens.BookNavScreens
import com.karthihegde.readlist.navigation.screens.GeneralScreens
import com.karthihegde.readlist.navigation.screens.Screens
import com.karthihegde.readlist.ui.BookDetailView
import com.karthihegde.readlist.ui.CollectionScreen
import com.karthihegde.readlist.ui.DiscoverScreen
import com.karthihegde.readlist.ui.ProgressView
import com.karthihegde.readlist.ui.stats.StatScreen
import com.karthihegde.readlist.viewmodels.BookViewModel
import com.karthihegde.readlist.viewmodels.ClickBookViewModel
import com.karthihegde.readlist.viewmodels.RetroViewModel
import com.karthihegde.readlist.viewmodels.SearchViewModel

/**
 * Main Navigation Composable
 */
@ExperimentalComposeUiApi
@Composable
fun Navigation(
    clickBookViewModel: ClickBookViewModel = hiltViewModel(),
    retroViewModel: RetroViewModel = hiltViewModel(),
    bookViewModel: BookViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = viewModel()
) {
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = Screens.Progress.route) {
        composable(route = Screens.Discover.route) {
            DiscoverScreen(
                retroViewModel = retroViewModel,
                navHostController = navHostController,
                searchViewModel = searchViewModel
            )
        }
        composable(route = BookNavScreens.DetailView.route + "/{item}", arguments = listOf(
            navArgument("item") {
                type = NavType.StringType
            }
        )) { args ->
            args.arguments?.getString("item")?.let {
                clickBookViewModel.onValueChange(it)
                BookDetailView(
                    clickBookViewModel = clickBookViewModel,
                    viewModel = bookViewModel,
                    navHostController = navHostController
                )
            }
        }
        composable(route = Screens.Collection.route) {
            CollectionScreen(viewModel = bookViewModel, navController = navHostController)
        }
        composable(route = Screens.Progress.route) {
            ProgressView(viewModel = bookViewModel, navController = navHostController)
        }
        composable(route = GeneralScreens.StatScreen.route) {
            StatScreen(viewModel = bookViewModel, navController = navHostController)
        }
    }

}

