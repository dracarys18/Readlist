package com.karthihegde.readlist.retrofit.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @param image
 * @param text
 */
@Parcelize
data class ReadingModes(
    val image: Boolean,
    val text: Boolean
) : Parcelable
