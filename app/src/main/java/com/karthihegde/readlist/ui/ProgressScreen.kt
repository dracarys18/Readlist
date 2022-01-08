package com.karthihegde.readlist.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Undo
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.karthihegde.readlist.database.BookData
import com.karthihegde.readlist.navigation.screens.BookNavScreens
import com.karthihegde.readlist.navigation.screens.GeneralScreens
import com.karthihegde.readlist.retrofit.data.ImageLinks
import com.karthihegde.readlist.viewmodels.BookViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * The Main Screen of Book Progress View
 *
 * @param navController NavHost Controller
 */
@Composable
fun ProgressView(viewModel: BookViewModel, navController: NavController) {
    val scaffoldState = rememberScaffoldState()
    val scaffoldCoroutineScope = rememberCoroutineScope()
    val group by viewModel.groupByStatus.collectAsState(initial = mapOf())
    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopProgressBar(navController)
    }, bottomBar = {
        BottomBar(navHostController = navController)
    }) { paddingValues ->
        val books by viewModel.getAllData.collectAsState(initial = null)
        var placeHolderState by remember {
            mutableStateOf(false)
        }
        books?.let { bookData ->
            placeHolderState = false
            if (bookData.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .padding(paddingValues)
                        .placeholder(
                            visible = placeHolderState,
                            highlight = PlaceholderHighlight.fade()
                        )
                ) {

                    group.forEach { (initial, bookList) ->
                        item {
                            Text(
                                text = initial.plus("(${bookList.size})"),
                                modifier = Modifier.padding(
                                    top = 10.dp,
                                    start = 8.dp,
                                    bottom = 5.dp
                                ),
                                color = Color(0xff1e88e5),
                                fontWeight = FontWeight.Black
                            )
                        }
                        items(bookList) {
                            BookProgress(
                                viewModel = viewModel,
                                navController = navController,
                                data = it,
                                scaffoldState = scaffoldState,
                                scaffoldCoroutineScope = scaffoldCoroutineScope
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
        } ?: run {
            placeHolderState = true
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
 *
 * @param navController
 */
@Composable
fun TopProgressBar(navController: NavController) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .padding(15.dp)
                .wrapContentHeight()
        ) {
            Text(
                text = "Progress",
                modifier = Modifier.align(Alignment.TopStart),
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(48.dp),
                onClick = { navController.navigate(GeneralScreens.StatScreen.route) }) {
                Icon(
                    Icons.Outlined.Analytics,
                    modifier = Modifier.wrapContentSize(),
                    contentDescription = null,
                )
            }
        }
    }
}

/**
 * Individual Composable for a book in Collection
 *
 * @param viewModel
 * @param navController
 * @param data
 * @param scaffoldCoroutineScope Coroutine scope of Scaffold lifetime
 * @param scaffoldState
 */
@Composable
fun BookProgress(
    viewModel: BookViewModel,
    navController: NavController,
    data: BookData,
    scaffoldState: ScaffoldState,
    scaffoldCoroutineScope: CoroutineScope
) {
    var editView by remember {
        mutableStateOf(false)
    }

    //EditView Code
    if (editView) {
        val context = LocalContext.current
        val focusRequester = remember { FocusRequester() }
        val bookData by viewModel.getBookFromId(id = data.id).collectAsState(initial = null)
        var newPage = 0
        var isError by remember {
            mutableStateOf(false)
        }
        var text by remember {
            mutableStateOf("")
        }
        val pagesRead = bookData?.pagesRead?.toString() ?: ""
        val totalPages = bookData?.totalPages ?: Int.MAX_VALUE
        val onDoneAction: () -> Unit = {
            if (!isError && text.isNotEmpty()) {
                scaffoldCoroutineScope.launch {
                    viewModel.updatePages(pages = newPage, id = data.id)
                }
                editView = false
            } else
                Toast.makeText(
                    context,
                    "Enter a Proper Page Number",
                    Toast.LENGTH_SHORT
                ).show()
        }
        AlertDialog(
            onDismissRequest = {
                editView = false
            },
            confirmButton = {
                TextButton(onClick = {
                    onDoneAction()
                }) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { editView = false }) {
                    Text(text = "Cancel")
                }
            },
            text = {
                OutlinedTextField(
                    value = text,
                    label = {
                        Text(text = "Pages Read")
                    },
                    onValueChange = { value ->
                        text = value
                        isError = try {
                            newPage = abs(value.toInt())
                            newPage > totalPages
                        } catch (e: NumberFormatException) {
                            true
                        }
                    },
                    placeholder = { Text(pagesRead, textAlign = TextAlign.Center) },
                    isError = isError,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrect = true,
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { onDoneAction() }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(100.dp)
                        .focusRequester(focusRequester = focusRequester),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }
        )
    }

    //Progress View Code
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
                    editView = true
                }) {
                    Icon(Icons.Rounded.Edit, contentDescription = null)
                }
                Spacer(modifier = Modifier.weight(1f, true))
                IconButton(modifier = Modifier.padding(end = 10.dp), onClick = {
                    scaffoldCoroutineScope.launch {
                        viewModel.delete(data.id)
                        val snackResult = scaffoldState.snackbarHostState.showSnackbar(
                            "Deleted the Book",
                            "Undo"
                        )
                        if (snackResult == SnackbarResult.ActionPerformed) {
                            viewModel.insert(data)
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
                        scaffoldCoroutineScope.launch {
                            val oldPageValue = data.pagesRead
                            val snackbarString: String
                            if (completed) {
                                snackbarString = "Reset the book status"
                                viewModel.updatePages(pages = 0, id = data.id)
                            } else {
                                snackbarString = "Finished Reading this Book"
                                viewModel.updatePages(
                                    pages = data.totalPages,
                                    id = data.id
                                )
                            }
                            val snackResult = scaffoldState.snackbarHostState.showSnackbar(
                                snackbarString,
                                "Undo"
                            )
                            if (snackResult == SnackbarResult.ActionPerformed) {
                                viewModel.updatePages(id = data.id, pages = oldPageValue)
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

