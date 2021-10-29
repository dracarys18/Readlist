package com.karthihegde.readlist.retrofit.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Pageview
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String, val label: String, val icon: ImageVector) {
    object Progress : Screens("progress_screen", "Progress", Icons.Filled.Book)
    object Discover : Screens("discover_screen", "Discover", Icons.Filled.Pageview)
    object Collection : Screens("collection_screen", "Collection", Icons.Filled.BookmarkAdded)
}
