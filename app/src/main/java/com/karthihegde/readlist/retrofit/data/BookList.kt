package com.karthihegde.readlist.retrofit.data

data class BookList(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)