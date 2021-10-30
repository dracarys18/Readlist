package com.karthihegde.readlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberImagePainter
import com.karthihegde.readlist.retrofit.data.BookList
import com.karthihegde.readlist.retrofit.data.Item
import com.karthihegde.readlist.retrofit.getBookFromSearch
import com.karthihegde.readlist.retrofit.navigation.Navigation
import com.karthihegde.readlist.retrofit.navigation.Screens
import com.karthihegde.readlist.ui.theme.ReadlistTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReadlistTheme {
                Navigation()
            }
        }
    }
}

@Composable
fun DiscoverScreen(navHostController: NavHostController) {
    val screens = listOf(Screens.Progress, Screens.Discover, Screens.Collection)
    Surface(color = MaterialTheme.colors.background) {
        val text = remember { mutableStateOf(TextFieldValue("")) }
        Scaffold(topBar = {
            SearchView(text = text)
        }, bottomBar = { BottomBar(navHostController, screens) }) {
            Column(modifier = Modifier.padding(it)) {
                DisplayResults(navHostController, resultList = booklist)
            }
        }
    }
}

@Composable
fun SearchResults(navHostController: NavHostController, item: Item) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { /* todo */ })
            .padding(
                start = 16.dp,
                top = 8.dp,
                end = 16.dp,
                bottom = 8.dp
            )
            .wrapContentWidth(Alignment.Start)
    ) {
        val imagelink = item.volumeInfo.imageLinks
        if (imagelink == null) {
            val placeholder =
                if (isSystemInDarkTheme()) R.drawable.ic_book_placeholder_dark else R.drawable.ic_book_placeholder
            Image(
                painter = painterResource(id = placeholder),
                contentDescription = "",
                modifier = Modifier.size(100.dp)
            )
        } else {
            Image(
                painter = rememberImagePainter(
                    item.volumeInfo.imageLinks.thumbnail.replace(
                        "http://",
                        "https://"
                    )
                ),
                contentDescription = "",
                modifier = Modifier.size(100.dp)
            )
        }

        Column {
            Text(
                text = item.volumeInfo.title,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h6
            )

            val authors = if (item.volumeInfo.authors != null)
                "Authors:- " + item.volumeInfo.authors.joinToString(",")
            else
                "Authors:- Unknown"
            Text(
                text = authors,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Composable
fun DisplayResults(navHostController: NavHostController, resultList: MutableState<BookList?>) {
    if (resultList.value != null) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            items(resultList.value!!.items.take(30)) { item ->
                SearchResults(navHostController = navHostController, item = item)
            }
        }
    }
}

@Composable
fun SearchView(text: MutableState<TextFieldValue>) {
    val focusmanager = LocalFocusManager.current
    val focusRequester = FocusRequester()
    var leadingIcon by remember {
        mutableStateOf(Icons.Filled.Search)
    }
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    TopAppBar(
        elevation = 8.dp,
        backgroundColor = Color.Transparent,
        contentColor = Color.White
    ) {
        TextField(
            value = text.value,
            onValueChange = { value ->
                text.value = value
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusEvent { state ->
                    leadingIcon = if (state.isFocused) {
                        Icons.Filled.Clear
                    } else {
                        Icons.Filled.Search
                    }
                }
                .fillMaxWidth(1f),
            leadingIcon = {
                IconButton(onClick = {
                    if (leadingIcon == Icons.Filled.Clear && text.value.text.isNotEmpty()) {
                        text.value = TextFieldValue("")
                    } else if (leadingIcon == Icons.Filled.Clear && text.value.text.isEmpty()) {
                        focusmanager.clearFocus(true)
                    }
                }) {
                    Icon(leadingIcon, contentDescription = "")
                }
            },
            textStyle = TextStyle(color = textColor),
            singleLine = true,
            placeholder = {
                Text(text = "Search Any Book You Want")
            },
            shape = RectangleShape,
            keyboardActions = KeyboardActions(onDone = {
                focusmanager.clearFocus(true)
                val scope = CoroutineScope(Job() + Dispatchers.IO)
                scope.launch {
                    getBookFromSearch(text.value.text)
                }
            }),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun BottomBar(navHostController: NavHostController, barlist: List<Screens>) {
    BottomAppBar {
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        BottomNavigation(backgroundColor = MaterialTheme.colors.background) {
            barlist.forEach {
                BottomNavigationItem(
                    icon = { Icon(it.icon, contentDescription = "") },
                    selected = currentRoute == it.route,
                    label = { Text(text = it.label) },
                    onClick = { /*TODO*/ },
                    alwaysShowLabel = false
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ReadlistTheme {
    }
}
