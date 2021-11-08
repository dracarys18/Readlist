package com.karthihegde.readlist.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.karthihegde.readlist.retrofit.data.BookList
import com.karthihegde.readlist.retrofit.data.Item

var booklist: MutableState<BookList?> = mutableStateOf(null)
var clickBook: MutableState<Item?> = mutableStateOf(null)
var collectionBooks = mutableListOf<Item?>()
