package com.karthihegde.readlist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.rounded.ClearAll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.karthihegde.readlist.navigation.screens.BookNavScreens
import com.karthihegde.readlist.retrofit.data.BookList
import com.karthihegde.readlist.retrofit.data.ImageLinks
import com.karthihegde.readlist.retrofit.data.Item
import com.karthihegde.readlist.utils.PLACEHOLDER_IMAGE
import com.karthihegde.readlist.utils.getBookFromSearch
import com.karthihegde.readlist.utils.getCurrencySymbol
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun SearchView() {
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()
    var leadingIcon by remember {
        mutableStateOf(Icons.Filled.Search)
    }
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    Surface(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(5.dp),
        contentColor = Color.White
    ) {
        val borderColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
        val text = SearchResults.text.value
        TextField(
            value = text,
            onValueChange = { value ->
                SearchResults.text.value = value
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
                .fillMaxWidth(1f)
                .padding(top = 5.dp, start = 3.dp, end = 3.dp)
                .border(1.dp, color = borderColor, shape = RoundedCornerShape(12.dp)),
            leadingIcon = {
                IconButton(onClick = {
                    if (leadingIcon == Icons.Filled.Clear && text.text.isNotEmpty()) {
                        SearchResults.text.value = TextFieldValue("")
                    } else if (leadingIcon == Icons.Filled.Clear && text.text.isEmpty()) {
                        focusManager.clearFocus(true)
                    }
                }) {
                    Icon(leadingIcon, contentDescription = "")
                }
            },
            trailingIcon = {
                SearchResults.bookList.value?.let {
                    IconButton(onClick = {
                        SearchResults.apply {
                            bookList.value = null
                            isError.value = false
                        }
                    }) {
                        Icon(Icons.Rounded.ClearAll, contentDescription = "Clear all Button")
                    }
                }
            },
            textStyle = TextStyle(color = textColor),
            singleLine = true,
            placeholder = {
                Text(text = "Search Any Book You Want")
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus(true)
                val scope = CoroutineScope(Job() + Dispatchers.IO)
                scope.launch {
                    getBookFromSearch(text.text)
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
fun SearchResults(navHostController: NavController, item: Item) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                navHostController.navigate(route = BookNavScreens.DetailView.withArgs(item.id))
            })
            .padding(
                start = 16.dp,
                top = 8.dp,
                end = 16.dp,
                bottom = 8.dp
            )
    ) {
        Row(horizontalArrangement = Arrangement.End) {
            Icon(
                Icons.Default.StarRate,
                contentDescription = null,
                modifier = Modifier.size(15.dp),
                tint = Color.Red
            )
            Text(
                text = item.volumeInfo.averageRating.toString(),
                style = MaterialTheme.typography.caption
            )
        }
        Row {
            val imageLink = item.volumeInfo.imageLinks
            BookImage(
                imageLink = imageLink,
                ActualImageModifier = Modifier.size(100.dp),
                PlaceHolderModifier = Modifier.size(100.dp)
            )
            Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                Text(
                    text = item.volumeInfo.title,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )

                val authors = if (item.volumeInfo.authors != null)
                    item.volumeInfo.authors.joinToString(",")
                else
                    "Unknown Author"
                Text(
                    text = authors,
                    color = Color(0xff1e88e5),
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.SemiBold
                )
                val code =
                    if (item.saleInfo.retailPrice == null) "" else getCurrencySymbol(item.saleInfo.retailPrice.currencyCode)
                        ?: ""
                val price =
                    if (item.saleInfo.retailPrice == null) "Unknown" else item.saleInfo.retailPrice.amount
                Text(text = code + price, style = MaterialTheme.typography.caption)
            }
        }
    }
}

@Composable
fun BookImage(
    imageLink: ImageLinks?,
    ActualImageModifier: Modifier,
    PlaceHolderModifier: Modifier
) {
    if (imageLink == null) {
        Image(
            painter = rememberImagePainter(PLACEHOLDER_IMAGE),
            contentDescription = "",
            modifier = PlaceHolderModifier,
        )
    } else {
        Image(
            painter = rememberImagePainter(
                imageLink.thumbnail.replace(
                    "http://",
                    "https://"
                )
            ),
            contentDescription = "",
            modifier = ActualImageModifier,
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun DiscoverScreen(navHostController: NavController) {
    Surface(color = MaterialTheme.colors.background) {
        Scaffold(topBar = {
            SearchView()
        }, bottomBar = { BottomBar(navHostController) }) {
            Column(modifier = Modifier.padding(it)) {
                DisplayResults(navHostController)
            }
        }
    }
}

@Composable
fun DisplayResults(navHostController: NavController) {
    SearchResults.bookList.value?.let { bookResults ->
        LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            items(bookResults.items) { item ->
                SearchResults(navHostController = navHostController, item = item)
            }
        }
    } ?: run {
        if (SearchResults.isError.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No Result Found for that Book",
                    fontSize = 25.sp,
                    style = MaterialTheme.typography.overline
                )
            }
        }
    }
}

object SearchResults {
    var text: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue(""))
    var bookList: MutableState<BookList?> = mutableStateOf(null)
    var isError: MutableState<Boolean> = mutableStateOf(false)
}
