package com.karthihegde.readlist.retrofit.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @param textSnippet
 */
@Parcelize
data class SearchInfo(
    val textSnippet: String
) : Parcelable
