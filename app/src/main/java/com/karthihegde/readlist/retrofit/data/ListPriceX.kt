package com.karthihegde.readlist.retrofit.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @param amountInMicros
 * @param currencyCode
 */
@Parcelize
data class ListPriceX(
    val amountInMicros: Long,
    val currencyCode: String
) : Parcelable
