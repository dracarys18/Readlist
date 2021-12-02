package com.karthihegde.readlist.navigation.screens

/**
 * @param route Route String for Navigation
 */
sealed class BookNavScreens(val route: String) {
    /**
     * DetailView Navigation Object
     */
    object DetailView : BookNavScreens("detail_view")

    /**
     * EditView Navigation Object
     */
    object EditView : BookNavScreens("edit_view")

    /**
     * @param args Args to append
     */
    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
