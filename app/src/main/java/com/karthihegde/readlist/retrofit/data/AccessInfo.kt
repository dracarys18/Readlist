package com.karthihegde.readlist.retrofit.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @param accessViewStatus
 * @param country
 * @param embeddable
 * @param epub
 * @param pdf
 * @param publicDomain
 * @param quoteSharingAllowed
 * @param textToSpeechPermission
 * @param viewability
 * @param webReaderLink
 */
@Parcelize
data class AccessInfo(
    val accessViewStatus: String,
    val country: String,
    val embeddable: Boolean,
    val epub: Epub,
    val pdf: Pdf,
    val publicDomain: Boolean,
    val quoteSharingAllowed: Boolean,
    val textToSpeechPermission: String,
    val viewability: String,
    val webReaderLink: String
) : Parcelable
