package com.karthihegde.readlist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.karthihegde.readlist.R
import com.karthihegde.readlist.book

@Composable
fun BookDetailView(navHostController: NavController) {
    var isLoading by remember {
        mutableStateOf(true)
    }
    Scaffold {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .placeholder(visible = isLoading, highlight = PlaceholderHighlight.shimmer())
        ) {
            item {
                book.value?.let {
                    //if value is not null isloading is false
                    isLoading = false

                    Card(
                        modifier = Modifier.fillMaxSize(),
                        shape = RectangleShape
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
                        ) {
                            IconButton(onClick = { navHostController.popBackStack() }) {
                                Icon(
                                    Icons.Filled.ArrowBackIosNew,
                                    contentDescription = "",
                                    tint = MaterialTheme.colors.onBackground,
                                    modifier = Modifier.shadow(elevation = 8.dp, clip = true)
                                )
                            }
                        }
                        Column(
                            modifier = Modifier.padding(50.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            val imagelink = book.value!!.volumeInfo.imageLinks
                            if (imagelink == null) {
                                val placeholder =
                                    if (isSystemInDarkTheme()) R.drawable.ic_book_placeholder_dark else R.drawable.ic_book_placeholder
                                Image(
                                    painter = painterResource(id = placeholder),
                                    contentDescription = "",
                                    modifier = Modifier.size(200.dp)
                                )
                            } else {
                                Image(
                                    painter = rememberImagePainter(
                                        imagelink.thumbnail.replace(
                                            "http://",
                                            "https://"
                                        )
                                    ),
                                    contentDescription = "",
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier
                                        .width(150.dp)
                                        .height(250.dp)
                                        .shadow(
                                            elevation = 16.dp,
                                            clip = true,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                )
                            }
                            Column(modifier = Modifier.padding(top = 5.dp)) {
                                Text(
                                    text = book.value!!.volumeInfo.title,
                                    color = MaterialTheme.colors.onBackground,
                                    style = MaterialTheme.typography.h5,
                                    fontWeight = FontWeight.Bold
                                )
                                val authors =
                                    if (book.value!!.volumeInfo.authors != null) book.value!!.volumeInfo.authors!!.joinToString(
                                        ","
                                    ) else "Unknown"

                                Text(
                                    text = "by $authors",
                                    style = MaterialTheme.typography.caption,
                                    modifier = Modifier
                                        .padding(top = 5.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
