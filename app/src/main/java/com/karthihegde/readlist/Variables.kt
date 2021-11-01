package com.karthihegde.readlist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.karthihegde.readlist.retrofit.data.BookList
import com.karthihegde.readlist.retrofit.data.Item

var booklist: MutableState<BookList?> = mutableStateOf(null)
var book: MutableState<Item?> = mutableStateOf(null)
