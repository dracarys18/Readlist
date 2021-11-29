package com.karthihegde.readlist.navigation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CollectionsBookmark
import androidx.compose.material.icons.rounded.Pageview
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Bottom Navigation Bar Screen
 *
 * @param route Router ID for the NavGraph
 * @param label Bottom Bar Label
 * @param icon Icon to Display in the bottom Navigation bar
 */
sealed class Screens(val route: String, val label: String, val icon: ImageVector) {
    object Progress : Screens("progress_screen", "Progress", Icons.Rounded.Sync)
    object Discover : Screens("discover_screen", "Discover", Icons.Rounded.Pageview)
    object Collection :
        Screens("collection_screen", "Collection", Icons.Rounded.CollectionsBookmark)
}
