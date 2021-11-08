package com.karthihegde.readlist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.karthihegde.readlist.navigation.screens.BookNavScreens
import com.karthihegde.readlist.retrofit.data.Item
import com.karthihegde.readlist.utils.PLACEHOLDER_IMAGE
import com.karthihegde.readlist.utils.clickBook

@Composable
fun CollectionScreen(navController: NavController) {
    //TODO("Replace with value from Firebase")
    val collectionlist =
        listOf("_J-NHwjl7N8C", "rDRcGP-fWIEC", "M_FnYanUOtgC", "fFFvswEACAAJ", "XFTfwc-QasEC")

    val numitems = LocalConfiguration.current.screenWidthDp / 150
    Scaffold(topBar = {
        TopBar()
    }, bottomBar = {
        BottomBar(navHostController = navController)
    }) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(collectionlist.chunked(numitems)) { rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    rowItems.forEachIndexed { index, book ->
                        BookCard(
                            item = clickBook.value,//fixme: Replace with 'book' later
                            modifier = Modifier.fillMaxWidth(1f / (numitems - index)),
                            onClick = {
                                navController.navigate(
                                    route = BookNavScreens.DetailView.withArgs(
                                        book
                                    )
                                )
                            })
                    }
                }
                Spacer(Modifier.height(14.dp))
            }
        }
    }
}

@Composable
fun BookCard(item: Item?, modifier: Modifier, onClick: () -> Unit) {
    item?.let { ite ->
        Column(modifier = modifier.clickable(onClick = onClick)) {
            Card {
                BookImage(
                    imageLink = ite.volumeInfo.imageLinks,
                    ActualImageModifier = Modifier.size(200.dp),
                    PlaceHolderModifier = Modifier.size(200.dp)
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = ite.volumeInfo.title,
                    maxLines = 1,
                    fontSize = 13.sp,
                    style = MaterialTheme.typography.caption
                )
            }
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
