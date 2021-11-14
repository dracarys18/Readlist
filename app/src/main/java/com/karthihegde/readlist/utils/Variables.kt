package com.karthihegde.readlist.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.karthihegde.readlist.retrofit.data.BookList
import com.karthihegde.readlist.retrofit.data.Item

var clickBook: MutableState<Item?> = mutableStateOf(null)

