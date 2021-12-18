package com.karthihegde.readlist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.karthihegde.readlist.database.BookData
import com.karthihegde.readlist.database.BookViewModel
import com.karthihegde.readlist.navigation.screens.BookNavScreens

/**
 * Composable function for displaying collection screen
 *
 * @param navController NavHost Controller
 */
@Composable
fun CollectionScreen(viewModel: BookViewModel, navController: NavController) {
    val collectionList by viewModel.getAllData.collectAsState(initial = null)
    val numItems = LocalConfiguration.current.screenWidthDp / 150
    Scaffold(topBar = {
        TopBar()
    }, bottomBar = {
        BottomBar(navHostController = navController)
    }) {
        if (!collectionList.isNullOrEmpty()) {
            LazyColumn(modifier = Modifier.padding(it)) {
                items(collectionList!!.chunked(numItems)) { rowItems ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    ) {
                        rowItems.forEachIndexed { index, book ->
                            BookCard(
                                data = book,
                                modifier = Modifier.fillMaxWidth(1f / (numItems - index)),
                                onClick = {
                                    navController.navigate(
                                        route = BookNavScreens.DetailView.withArgs(
                                            book.id
                                        )
                                    )
                                })
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Nothing here so far",
                    fontSize = 25.sp,
                    style = MaterialTheme.typography.overline
                )
            }
        }
    }
}

/**
 * Composable function for making the vertical grid view
 *
 * @param data BookData from the Database
 * @param modifier Compose modifier to fit book as a grid
 * @param onClick onClick listener for the BookCard
 */
@Composable
fun BookCard(data: BookData, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .width(125.dp)
            .height(195.dp)
    ) {
        Box {
            Image(
                painter = rememberImagePainter(
                    data.imageUrl.replace(
                        "http://",
                        "https://"
                    )
                ),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colors.background
                            ),
                            startY = 25f
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = data.bookName,
                    maxLines = 2,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.caption,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Composable function for TopAppBar collection screen
 */
@Composable
fun TopBar() {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(15.dp)) {
            Text(
                text = "Collection",
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(top = 20.dp, bottom = 15.dp, end = 10.dp)
        ) {
            //TODO: Something Cool Here
        }
    }
}
