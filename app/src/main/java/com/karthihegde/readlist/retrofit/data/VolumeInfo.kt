package com.karthihegde.readlist.retrofit.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @param allowAnonLogging
 * @param authors
 * @param averageRating
 * @param canonicalVolumeLink
 * @param categories
 * @param contentVersion
 * @param description
 * @param imageLinks
 * @param industryIdentifiers
 * @param infoLink
 * @param language
 * @param maturityRating
 * @param pageCount
 * @param panelizationSummary
 * @param previewLink
 * @param printType
 * @param publishedDate
 * @param publisher
 * @param ratingsCount
 * @param readingModes
 * @param subtitle
 * @param title
 */
@Parcelize
data class VolumeInfo(
    val allowAnonLogging: Boolean,
    val authors: List<String>?,
    val averageRating: Float,
    val canonicalVolumeLink: String,
    val categories: List<String>?,
    val contentVersion: String,
    val description: String?,
    val imageLinks: ImageLinks?,
    val industryIdentifiers: List<IndustryIdentifier>,
    val infoLink: String,
    val language: String,
    val maturityRating: String,
    val pageCount: Int,
    val panelizationSummary: PanelizationSummary,
    val previewLink: String,
    val printType: String,
    val publishedDate: String,
    val publisher: String,
    val ratingsCount: Int,
    val readingModes: ReadingModes,
    val subtitle: String?,
    val title: String
) : Parcelable
