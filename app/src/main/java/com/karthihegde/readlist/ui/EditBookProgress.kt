package com.karthihegde.readlist.ui

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.karthihegde.readlist.database.BookViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * Page to edit the pages of a book
 *
 * @param viewModel VieModel
 * @param id Unique ID of the Book
 * @param navController NavHost Controller
 */
@Composable
fun EditBookProgress(viewModel: BookViewModel, navController: NavController, id: String) {
    val context = LocalContext.current
    var newPage = 0
    val bookData by viewModel.getBookFromId(id = id).collectAsState(initial = null)
    val scope = rememberCoroutineScope { Dispatchers.IO }
    val pagesRead = bookData?.pagesRead?.toString() ?: ""
    var isError by remember {
        mutableStateOf(false)
    }
    var text by remember {
        mutableStateOf("")
    }
    val borderColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
    val totalPages = bookData?.totalPages ?: Int.MAX_VALUE
    val onDoneAction: () -> Unit = {
        if (!isError && text.isNotEmpty()) {
            scope.launch {
                viewModel.updatePages(pages = newPage, id = id)
            }
            navController.popBackStack()
        } else
            Toast.makeText(
                context,
                "Enter a Proper Page Number",
                Toast.LENGTH_SHORT
            ).show()
    }
    Scaffold(topBar = { EditScreenTopAppBar(navController = navController) }) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Pages Read:",
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(20.dp)
                )
                Row {
                    OutlinedTextField(
                        value = text,
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
                            .wrapContentHeight()
                            .fillMaxWidth(0.6f)
                            .padding(top = 10.dp)
                            .border(1.dp, color = borderColor, shape = RoundedCornerShape(12.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.background,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    Text(
                        text = "/$totalPages",
                        fontSize = 20.sp,
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
            val backroundColor = if (isSystemInDarkTheme()) Color(0xff1565c0) else Color(0xff5e92f3)
            Row {
                Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                OutlinedButton(
                    onClick = {
                        onDoneAction()
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(backgroundColor = backroundColor)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "")
                }
            }
        }
    }
}

/**
 * EditScreen Top App Bar
 *
 * @param navController navcontroller
 */
@Composable
fun EditScreenTopAppBar(navController: NavController) {
    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                Icons.Filled.ArrowBackIosNew,
                contentDescription = "",
                tint = MaterialTheme.colors.onBackground,
                modifier = Modifier.shadow(elevation = 8.dp, clip = true)
            )
        }
    }
}
