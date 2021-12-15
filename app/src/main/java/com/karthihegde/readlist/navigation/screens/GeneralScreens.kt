package com.karthihegde.readlist.navigation.screens

/**
 * Generic Screens without args and Multiple attributes
 *
 * @param route Route of the Screen
 */
sealed class GeneralScreens(val route: String) {
    /**
     * Stat Screen Object
     */
    object StatScreen : GeneralScreens("stat_screen")
}
