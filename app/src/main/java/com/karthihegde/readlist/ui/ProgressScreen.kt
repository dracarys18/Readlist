package com.karthihegde.readlist.ui

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun ProgressView(navController: NavController) {
    Scaffold(bottomBar = {
        BottomBar(navHostController = navController)
    }) {
        Text(text = "This is Progress Screen")
    }
}
