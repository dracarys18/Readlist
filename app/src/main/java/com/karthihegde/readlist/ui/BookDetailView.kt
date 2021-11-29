package com.karthihegde.readlist.ui

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.karthihegde.readlist.database.BookData
import com.karthihegde.readlist.database.BookDatabase
import com.karthihegde.readlist.database.BookViewModel
import com.karthihegde.readlist.retrofit.data.Item
import com.karthihegde.readlist.utils.PLACEHOLDER_IMAGE
import com.karthihegde.readlist.utils.clickBook
import com.karthihegde.readlist.utils.getCurrencySymbol
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Composable Function to Render Detailed View for a Book which was clicked
 *
 * @param navHostController Navigation Host Controller
 */
@Composable
fun BookDetailView(navHostController: NavController) {
    var isLoading by remember {
        mutableStateOf(true)
    }
    val context = LocalContext.current
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .placeholder(visible = isLoading, highlight = PlaceholderHighlight.shimmer())
        ) {
            item {
                clickBook.value?.let { item ->
                    //if value is not null isLoading is false
                    isLoading = false
                    val uriIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.volumeInfo.infoLink))
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = RectangleShape,
                    ) {
                        BackAndCollButton(item, navHostController)
                        Column {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(50.dp),
                            ) {
                                TitleAndAuthor(item)
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(top = 30.dp, start = 8.dp)
                                ) {
                                    InfoTable(
                                        title = "Rating",
                                        value = item.volumeInfo.averageRating.toString()
                                    )
                                    Spacer(modifier = Modifier.padding(10.dp))
                                    InfoTable(
                                        title = "Pages",
                                        value = item.volumeInfo.pageCount.toString()
                                    )
                                    Spacer(modifier = Modifier.padding(10.dp))
                                    InfoTable(title = "Language", value = item.volumeInfo.language)
                                    Spacer(modifier = Modifier.padding(10.dp))
                                    val price = if (item.saleInfo.retailPrice != null) {
                                        val symbol =
                                            getCurrencySymbol(item.saleInfo.retailPrice.currencyCode)
                                                ?: ""
                                        symbol.plus(
                                            item.saleInfo.retailPrice.amount
                                        )
                                    } else "Unknown"
                                    InfoTable(title = "Price", value = price)
                                }
                                Spacer(modifier = Modifier.padding(10.dp))
                                IconButton(onClick = {
                                    context.startActivity(uriIntent)
                                }) {
                                    Row {
                                        Icon(
                                            Icons.Default.ReadMore,
                                            contentDescription = "Read More"
                                        )
                                        Spacer(modifier = Modifier.padding(5.dp))
                                        Text(
                                            text = "Read Book",
                                            style = MaterialTheme.typography.button
                                        )
                                    }
                                }
                            }
                            val html = item.volumeInfo.description ?: "Not Provided"
                            val description =
                                HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
                            InfoContent(title = "Description", content = description.toString())
                            Spacer(modifier = Modifier.padding(10.dp))
                            val categories =
                                item.volumeInfo.categories?.joinToString("")
                                    ?: "Not Specified"
                            InfoContent(title = "Categories", content = categories)
                            Spacer(modifier = Modifier.padding(10.dp))
                            InfoContent(title = "Publisher", content = item.volumeInfo.publisher)
                            Spacer(modifier = Modifier.padding(10.dp))
                            InfoContent(
                                title = "Date Of Publishing",
                                content = item.volumeInfo.publishedDate
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable function to Display Title and Author as well as book thumbnail in the display screen
 *
 * @param it of Type Item
 */
@Composable
fun TitleAndAuthor(it: Item) {
    val imageLink = it.volumeInfo.imageLinks
    val actualImageModifier = Modifier
        .width(150.dp)
        .height(250.dp)
        .shadow(
            elevation = 16.dp,
            clip = true,
            shape = RoundedCornerShape(16.dp)
        )
    val placeholderModifier = Modifier.size(200.dp)
    BookImage(
        imageLink = imageLink,
        ActualImageModifier = actualImageModifier,
        PlaceHolderModifier = placeholderModifier
    )
    Column(modifier = Modifier.padding(top = 5.dp)) {
        Text(
            text = it.volumeInfo.title,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
    }
    val authors =
        if (it.volumeInfo.authors != null) it.volumeInfo.authors.joinToString(
            ","
        ) else "Unknown"

    Text(
        text = "by $authors",
        style = MaterialTheme.typography.caption,
        modifier = Modifier
            .padding(top = 5.dp)
    )
}

/**
 * Composable Function to Display the Back And Add to Collection and Share Button
 *
 * @param item of Type Item which contains all book info
 * @param navHostController NavHost Controller
 */
@Composable
fun BackAndCollButton(item: Item, navHostController: NavController) {
    val context = LocalContext.current
    val viewModel = BookViewModel(context.applicationContext as Application)
    val ifExists by viewModel.dao.checkIfBookExists(item.id).observeAsState()
    val bookmarkIcon =
        if (ifExists == true) Icons.Filled.BookmarkAdded else Icons.Filled.Bookmark
    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
    ) {
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
            onClick = {
                val text =
                    "Hey I have been reading this book called \"${item.volumeInfo.title}\". I think It's awesome you should definitely check it out\n ${item.volumeInfo.infoLink}"
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, text)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 50.dp)
        ) {
            Icon(
                Icons.Filled.Share,
                contentDescription = "Share Button"
            )
        }
        IconButton(
            onClick = {
                val imageLink = item.volumeInfo.imageLinks?.thumbnail ?: PLACEHOLDER_IMAGE
                val author = item.volumeInfo.authors?.joinToString(",") ?: "Unknown"
                val calender = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("MMM dd,yyyy", Locale.getDefault())
                val date = dateFormat.format(calender.time)
                val data =
                    BookData(
                        id = item.id,
                        bookName = item.volumeInfo.title,
                        imageUrl = imageLink,
                        author = author,
                        totalPages = item.volumeInfo.pageCount,
                        insertDate = date,
                    )
                val dao = BookDatabase.getInstance(context).bookDatabaseDo
                val scope = CoroutineScope(Job() + Dispatchers.IO)
                scope.launch {
                    if (ifExists == true)
                        dao.delete(data.id)
                    else
                        dao.insert(data)
                }
            },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                bookmarkIcon,
                contentDescription = "",
                tint = MaterialTheme.colors.onBackground,
                modifier = Modifier.shadow(elevation = 8.dp, clip = true)
            )
        }
    }
}

/**
 * Composable function which acts like a Table to Display book info
 *
 * @param title Title of the table Column
 * @param value Value to Display in the Table
 */
@Composable
fun InfoTable(title: String, value: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.overline,
            modifier = Modifier.padding(2.dp),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Composable function to Display a para with heading and content
 *
 * @param title Heading of the Para
 * @param content Content inside the para
 */
@Composable
fun InfoContent(title: String, content: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(start = 8.dp),
        )
        Text(
            text = "\t$content",
            style = MaterialTheme.typography.caption,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 5.dp)
        )
    }
}
