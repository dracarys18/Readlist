package com.karthihegde.readlist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.karthihegde.readlist.retrofit.data.BookList
import com.karthihegde.readlist.retrofit.getBookFromSearch
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
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(topBar = {
                        TopAppBar(
                            elevation = 8.dp,
                            backgroundColor = Color.Transparent,
                            contentColor = Color.White
                        ) {
                            MainScreen()
                            Column(modifier = Modifier.padding(200.dp)) {
                                DisplayResults(booklist)
                            }
                        }
                    }) {
                    }
                }
            }
        }
    }
}


@Composable
fun MainScreen() {
    val text = remember { mutableStateOf(TextFieldValue("")) }
    SearchView(text = text)
}

@Composable
fun DisplayResults(resultList: MutableState<BookList?>) {
    if (resultList.value != null) {
        Log.d("MainActivity", resultList.value!!.items.first().volumeInfo.title)
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
            .fillMaxWidth(1f)
            .padding(start = 5.dp, end = 5.dp, top = 2.dp, bottom = 2.dp),
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ReadlistTheme {
        MainScreen()
    }
}