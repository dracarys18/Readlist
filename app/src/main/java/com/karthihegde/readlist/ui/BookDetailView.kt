package com.karthihegde.readlist.ui

import android.os.Build
import android.text.Html
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.karthihegde.readlist.R
import com.karthihegde.readlist.book
import com.karthihegde.readlist.retrofit.getCurrencySymbol

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
                        Box(modifier = Modifier.padding(top = 8.dp)) {
                            IconButton(
                                onClick = { navHostController.popBackStack() },
                                modifier = Modifier.align(Alignment.TopStart)
                            ) {
                                Icon(
                                    Icons.Filled.ArrowBackIosNew,
                                    contentDescription = "",
                                    tint = MaterialTheme.colors.onBackground,
                                    modifier = Modifier.shadow(elevation = 8.dp, clip = true)
                                )
                            }
                            IconButton(
                                onClick = { /*TODO*/ },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    Icons.Outlined.Bookmarks,
                                    contentDescription = "",
                                    tint = MaterialTheme.colors.onBackground,
                                    modifier = Modifier.shadow(elevation = 8.dp, clip = true)
                                )
                            }
                        }
                        Column {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(50.dp),
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
                                }
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
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(top = 30.dp, start = 8.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "Rating",
                                            style = MaterialTheme.typography.overline,
                                            modifier = Modifier.padding(2.dp),
                                        )
                                        Text(
                                            text = book.value!!.volumeInfo.averageRating.toString(),
                                            style = MaterialTheme.typography.h5,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.padding(10.dp))
                                    Column {
                                        Text(
                                            text = "Pages",
                                            style = MaterialTheme.typography.overline,
                                            modifier = Modifier.padding(2.dp)
                                        )
                                        Text(
                                            text = book.value!!.volumeInfo.pageCount.toString(),
                                            style = MaterialTheme.typography.h5,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.padding(10.dp))
                                    Column {
                                        Text(
                                            text = "Language",
                                            style = MaterialTheme.typography.overline,
                                            modifier = Modifier.padding(2.dp)
                                        )
                                        Text(
                                            text = book.value!!.volumeInfo.language,
                                            style = MaterialTheme.typography.h5,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.padding(10.dp))
                                    Column {
                                        Text(
                                            text = "Price",
                                            style = MaterialTheme.typography.overline,
                                            modifier = Modifier.padding(2.dp),
                                        )
                                        val price = if (book.value!!.saleInfo.retailPrice != null) {
                                            val symbol =
                                                getCurrencySymbol(book.value!!.saleInfo.retailPrice!!.currencyCode)
                                                    ?: ""
                                            symbol.plus(
                                                book.value!!.saleInfo.retailPrice!!.amount
                                            )
                                        } else "Unknown"
                                        Text(
                                            text = price,
                                            style = MaterialTheme.typography.h5,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.h5,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(start = 5.dp),
                            )
                            val html = book.value!!.volumeInfo.description ?: "Not Provided"
                            val description = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
                            } else {
                                Html.fromHtml(html)
                            }
                            Text(
                                text = "\t$description",
                                style = MaterialTheme.typography.caption,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(start = 5.dp, top = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
