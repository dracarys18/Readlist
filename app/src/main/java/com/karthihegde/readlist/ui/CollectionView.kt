package com.karthihegde.readlist.ui

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.karthihegde.readlist.database.BookData
import com.karthihegde.readlist.database.BookViewModel
import com.karthihegde.readlist.navigation.screens.BookNavScreens
import com.karthihegde.readlist.utils.PLACEHOLDER_IMAGE

@Composable
fun CollectionScreen(navController: NavController) {
    val viewModel = BookViewModel(LocalContext.current.applicationContext as Application)
    val collectionList by viewModel.getAllData.observeAsState()
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
            Column {
                Text(text = "Nothing here so far", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun BookCard(data: BookData, modifier: Modifier, onClick: () -> Unit) {
    Column(modifier = modifier.clickable(onClick = onClick)) {
        Card {
            Image(
                painter = rememberImagePainter(
                    data.imageUrl.replace(
                        "http://",
                        "https://"
                    )
                ),
                contentDescription = "",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = data.bookName,
                maxLines = 1,
                fontSize = 13.sp,
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Composable
fun TopBar() {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(15.dp)) {
            Text(
                text = "Browse",
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(top = 20.dp, bottom = 15.dp, end = 10.dp)
        ) {
            Image(
                painter = rememberImagePainter(PLACEHOLDER_IMAGE),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
            )
        }
    }
}
