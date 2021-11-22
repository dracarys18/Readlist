package com.karthihegde.readlist.retrofit.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RetailPrice(
    val amountInMicros: Long,
    val currencyCode: String
) : Parcelable
