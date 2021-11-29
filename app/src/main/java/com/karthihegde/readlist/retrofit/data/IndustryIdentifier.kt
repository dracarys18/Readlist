package com.karthihegde.readlist.retrofit.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @param identifier
 * @param type
 */
@Parcelize
data class IndustryIdentifier(
    val identifier: String,
    val type: String
) : Parcelable
