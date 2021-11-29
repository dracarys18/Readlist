package com.karthihegde.readlist.retrofit.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @param containsEpubBubbles
 * @param containsImageBubbles
 */
@Parcelize
data class PanelizationSummary(
    val containsEpubBubbles: Boolean,
    val containsImageBubbles: Boolean
) : Parcelable
