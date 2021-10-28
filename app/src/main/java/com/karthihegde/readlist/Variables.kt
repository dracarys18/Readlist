package com.karthihegde.readlist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.karthihegde.readlist.retrofit.data.BookList

var booklist: MutableState<BookList?> = mutableStateOf(null)