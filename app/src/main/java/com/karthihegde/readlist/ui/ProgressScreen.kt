package com.karthihegde.readlist.ui

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
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

@Composable
fun ProgressView(navController: NavController) {
    Scaffold(topBar = {
        TopProgressBar()
    }, bottomBar = {
        BottomBar(navHostController = navController)
    }) {
        val viewModel = BookViewModel(LocalContext.current.applicationContext as Application)
        val books by viewModel.getAllData.observeAsState()
        if (!books.isNullOrEmpty()) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(books!!) {
                    BookProgress(navController = navController, data = it)
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

@Composable
fun BookProgress(navController: NavController, data: BookData) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                navController.navigate(route = BookNavScreens.DetailView.withArgs(data.id))
            })
            .padding(
                start = 16.dp,
                top = 8.dp,
                end = 16.dp,
                bottom = 8.dp
            )
            .fillMaxWidth()
    ) {
        Column {
            BookImage(
                imageLink = ImageLinks("", data.imageUrl),
                ActualImageModifier = Modifier.size(100.dp),
                PlaceHolderModifier = Modifier.size(100.dp)
            )
            TextButton(onClick = {
                navController.navigate(BookNavScreens.EditView.withArgs(data.id))
            }) {
                Text(text = "Edit")
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(
                text = data.bookName,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h6,
                overflow = TextOverflow.Ellipsis
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
            val progress = (data.pagesRead.toFloat() / data.totalPages.toFloat())
            Text(
                text = "${(progress * 100).toInt()}%",
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.overline
            )
            LinearProgressIndicator(
                progress,
                color = Color.Blue,
                backgroundColor = MaterialTheme.colors.onBackground
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = data.insertDate,
                style = MaterialTheme.typography.overline,
                color = MaterialTheme.colors.onBackground,
                maxLines = 2
            )
            Spacer(modifier = Modifier.padding(20.dp))
            IconButton(onClick = {
                val dao = BookDatabase.getInstance(context)
                val scope = CoroutineScope(Job() + Dispatchers.IO)
                scope.launch {
                    dao.bookDatabaseDo.updatePages(pages = data.totalPages, id = data.id)
                }
            }) {
                Icon(Icons.Default.DoneAll, contentDescription = "")
            }
        }
    }
}
