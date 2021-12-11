package com.karthihegde.readlist.ui

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Undo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.karthihegde.readlist.database.BookData
import com.karthihegde.readlist.database.BookDatabase
import com.karthihegde.readlist.database.BookViewModel
import com.karthihegde.readlist.navigation.screens.BookNavScreens
import com.karthihegde.readlist.retrofit.data.ImageLinks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * The Main Screen of Book Progress View
 *
 * @param navController NavHost Controller
 */
@Composable
fun ProgressView(navController: NavController) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopProgressBar()
    }, bottomBar = {
        BottomBar(navHostController = navController)
    }) { paddingValues ->
        val viewModel = BookViewModel(LocalContext.current.applicationContext as Application)
        val books by viewModel.getAllData.observeAsState()
        if (!books.isNullOrEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(paddingValues)
            ) {
                val group = books!!.groupBy { bookData ->
                    val status =
                        when (bookData.pagesRead) {
                            0 -> "To Read"
                            bookData.totalPages -> "Finished"
                            else -> "In Progress"
                        }
                    status
                }
                group.forEach { (initial, bookList) ->
                    item {
                        Text(
                            text = initial,
                            modifier = Modifier.padding(top = 10.dp, start = 8.dp, bottom = 5.dp),
                            color = Color(0xff1e88e5),
                            fontWeight = FontWeight.Black
                        )
                    }
                    items(bookList) {
                        BookProgress(
                            navController = navController,
                            data = it,
                            scaffoldState = scaffoldState
                        )
                    }
                    item {
                        ReadListDivider()
                    }
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
 * Divider between 3 groups of collection
 */
@Composable
fun ReadListDivider() {
    val dividerColor =
        if (isSystemInDarkTheme()) Color(0x1fffffff) else Color(0xFF35303C)
    Divider(
        thickness = 1.dp,
        startIndent = 0.dp,
        color = dividerColor.copy(alpha = 0.12f)
    )
}

/**
 * TopApp Bar for Progress Screen
 */
@Composable
fun TopProgressBar() {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(15.dp)) {
            Text(
                text = "Progress",
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Individual Composable for a book in Collection
 */
@Composable
fun BookProgress(navController: NavController, data: BookData, scaffoldState: ScaffoldState) {
    val context = LocalContext.current
    val dao = BookDatabase.getInstance(context)
    val scope = CoroutineScope(Job() + Dispatchers.IO)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                navController.navigate(route = BookNavScreens.DetailView.withArgs(data.id))
            })
    ) {
        Text(
            text = data.insertDate,
            style = MaterialTheme.typography.overline,
            color = MaterialTheme.colors.onBackground,
            maxLines = 2,
            modifier = Modifier.padding(end = 8.dp),
            textAlign = TextAlign.Right
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(modifier = Modifier.padding(top = 5.dp)) {
                BookImage(
                    imageLink = ImageLinks("", data.imageUrl),
                    ActualImageModifier = Modifier.size(100.dp),
                    PlaceHolderModifier = Modifier.size(100.dp)
                )
                Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                    Text(
                        text = data.bookName,
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.h6,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = data.author,
                        color = MaterialTheme.colors.secondary,
                        style = MaterialTheme.typography.caption
                    )
                    Text(
                        text = "${data.pagesRead}/${data.totalPages}",
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.overline
                    )
                    if (data.pagesRead != 0) {
                        val progress = (data.pagesRead.toFloat() / data.totalPages.toFloat())
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 7.dp),
                            text = "${(progress * 100).toInt()}%",
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.caption,
                            textAlign = TextAlign.Right
                        )
                        LinearProgressIndicator(
                            progress,
                            color = Color(0xff1e88e5),
                            modifier = Modifier
                                .height(2.dp)
                                .fillMaxWidth(1f),
                            backgroundColor = Color.White
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    navController.navigate(BookNavScreens.EditView.withArgs(data.id))
                }) {
                    Icon(Icons.Rounded.Edit, contentDescription = null)
                }
                Spacer(modifier = Modifier.weight(1f, true))
                IconButton(modifier = Modifier.padding(end = 10.dp), onClick = {
                    scope.launch {
                        dao.bookDatabaseDo.delete(data.id)
                        val snackResult = scaffoldState.snackbarHostState.showSnackbar(
                            "Deleted the Book",
                            "Undo"
                        )
                        if (snackResult == SnackbarResult.ActionPerformed) {
                            dao.bookDatabaseDo.insert(data)
                        }
                    }
                }) {
                    Icon(
                        Icons.Rounded.Delete,
                        contentDescription = null,
                    )
                }
                val completed = data.pagesRead == data.totalPages
                IconButton(
                    modifier = Modifier.padding(end = 5.dp),
                    onClick = {
                        scope.launch {
                            val oldPageValue = data.pagesRead
                            val snackbarString: String
                            if (completed) {
                                snackbarString = "Reset the book status"
                                dao.bookDatabaseDo.updatePages(pages = 0, id = data.id)
                            } else {
                                snackbarString = "Finished Reading this Book"
                                dao.bookDatabaseDo.updatePages(
                                    pages = data.totalPages,
                                    id = data.id
                                )
                            }
                            val snackResult = scaffoldState.snackbarHostState.showSnackbar(
                                snackbarString,
                                "Undo"
                            )
                            if (snackResult == SnackbarResult.ActionPerformed) {
                                dao.bookDatabaseDo.updatePages(id = data.id, pages = oldPageValue)
                            }
                        }
                    }) {
                    val icon =
                        if (completed) Icons.Rounded.Undo else Icons.Rounded.DoneAll
                    Icon(
                        icon,
                        contentDescription = "",
                        tint = MaterialTheme.colors.onBackground
                    )
                }
            }
        }
    }
}

