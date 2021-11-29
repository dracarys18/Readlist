package com.karthihegde.readlist.retrofit.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @param smallThumbnail
 * @param thumbnail
 */
@Parcelize
data class ImageLinks(
    val smallThumbnail: String,
    val thumbnail: String
) : Parcelable
