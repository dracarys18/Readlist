package com.karthihegde.readlist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.karthihegde.readlist.database.BookViewModel
import com.karthihegde.readlist.navigation.screens.BookNavScreens
import com.karthihegde.readlist.navigation.screens.GeneralScreens
import com.karthihegde.readlist.navigation.screens.Screens
import com.karthihegde.readlist.retrofit.ClickBookViewModel
import com.karthihegde.readlist.retrofit.RetroViewModel
import com.karthihegde.readlist.ui.*
import com.karthihegde.readlist.ui.stats.StatScreen

/**
 * Main Navigation Composable
 */
@ExperimentalComposeUiApi
@Composable
fun Navigation(
    clickBookViewModel: ClickBookViewModel = hiltViewModel(),
    retroViewModel: RetroViewModel = hiltViewModel(),
    viewModel: BookViewModel = hiltViewModel()
) {
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = Screens.Progress.route) {
        composable(route = Screens.Discover.route) {
            DiscoverScreen(retroViewModel = retroViewModel, navHostController = navHostController)
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
                    viewModel = viewModel,
                    navHostController = navHostController
                )
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

