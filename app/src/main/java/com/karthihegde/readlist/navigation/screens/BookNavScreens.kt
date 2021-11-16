package com.karthihegde.readlist.navigation.screens

sealed class BookNavScreens(val route: String) {
    object DetailView : BookNavScreens("detail_view")
    object EditView : BookNavScreens("edit_view")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
