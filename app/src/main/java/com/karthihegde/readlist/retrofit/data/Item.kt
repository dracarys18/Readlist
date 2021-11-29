package com.karthihegde.readlist.retrofit.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @param accessInfo
 * @param etag
 * @param id
 * @param kind
 * @param saleInfo
 * @param searchInfo
 * @param selfLink
 * @param volumeInfo
 */
@Parcelize
data class Item(
    val accessInfo: AccessInfo,
    val etag: String,
    val id: String,
    val kind: String,
    val saleInfo: SaleInfo,
    val searchInfo: SearchInfo,
    val selfLink: String,
    val volumeInfo: VolumeInfo
) : Parcelable
