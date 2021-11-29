package com.karthihegde.readlist.retrofit.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @param acsTokenLink
 * @param isAvailable
 */
@Parcelize
data class Epub(
    val acsTokenLink: String,
    val isAvailable: Boolean
) : Parcelable
