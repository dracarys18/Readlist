package com.karthihegde.readlist.retrofit.navigation.screens

sealed class BookNavScreens(val route: String) {
    object SearchView : BookNavScreens("search_view")
    object DetailView : BookNavScreens("detail_view")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}