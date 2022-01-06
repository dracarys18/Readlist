package com.karthihegde.readlist.retrofit.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @param items
 * @param kind
 * @param totalItems
 */
@Parcelize
data class BookList(
    val items: List<Item>?,
    val kind: String,
    val totalItems: Int
) : Parcelable
