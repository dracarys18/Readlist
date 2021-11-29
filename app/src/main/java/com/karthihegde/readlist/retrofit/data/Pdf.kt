package com.karthihegde.readlist.retrofit.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @param isAvailable
 */
@Parcelize
data class Pdf(
    val isAvailable: Boolean
) : Parcelable
