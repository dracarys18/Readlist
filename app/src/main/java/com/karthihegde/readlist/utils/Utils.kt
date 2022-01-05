package com.karthihegde.readlist.utils

import com.karthihegde.readlist.database.BookData
import java.util.*

/**
 * @param code Currency Code
 * @return Currency Symbol
 */
fun getCurrencySymbol(code: String?): String? {
    val cur = Currency.getInstance(code)
    return cur.symbol
}

/**
 * Extension function for grouping [List<BookData>]
 *
 * @return List of Books grouped according to their reading status
 */
fun List<BookData>.groupByStatus(): Map<String, List<BookData>> {
    return groupBy { bookData ->
        val status =
            when (bookData.pagesRead) {
                0 -> "To Read"
                bookData.totalPages -> "Finished"
                else -> "In Progress"
            }
        status
    }
}
