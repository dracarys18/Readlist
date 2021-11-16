package com.karthihegde.readlist.navigation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Pageview
import androidx.compose.material.icons.filled.Sync
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String, val label: String, val icon: ImageVector) {
    object Progress : Screens("progress_screen", "Progress", Icons.Filled.Sync)
    object Discover : Screens("discover_screen", "Discover", Icons.Filled.Pageview)
    object Collection : Screens("collection_screen", "Collection", Icons.Filled.CollectionsBookmark)
}
