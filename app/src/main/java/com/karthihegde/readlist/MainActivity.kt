package com.karthihegde.readlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.karthihegde.readlist.navigation.Navigation
import com.karthihegde.readlist.ui.theme.ReadlistTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity Class
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReadlistTheme {
                val systemUiController = rememberSystemUiController()
                if (isSystemInDarkTheme()) {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent
                    )
                } else {
                    systemUiController.setSystemBarsColor(
                        color = Color.White
                    )
                }
                Navigation()
            }
        }
    }
}

/**
 * Composable Preview Function
 */
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ReadlistTheme {
    }
}
