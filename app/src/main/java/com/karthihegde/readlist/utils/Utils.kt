package com.karthihegde.readlist.utils

import android.util.Log
import com.karthihegde.readlist.database.BookData
import com.karthihegde.readlist.retrofit.RetrofitService
import com.karthihegde.readlist.retrofit.data.Item
import com.karthihegde.readlist.ui.SearchResults
import java.util.*

/**
 * Get Booklist from Searched Query
 *
 * @param query Book Search Query
 * @return Returns updated SearchResults object
 */
suspend fun getBookFromSearch(query: String): SearchResults {
    val retroService = RetrofitService.createRetroInterface(RetrofitService.getRetrofit(BASE_URL))
    return try {
        SearchResults.apply {
            bookList.value = retroService.getBooks(query)
            isError.value = false
        }
    } catch (e: Exception) {
        Log.d("bookfromsearch", e.message, e.cause)
        SearchResults.apply {
            bookList.value = null
            isError.value = true
        }
    }
}

/**
 * Get [Item] object of the Book from the Book ID
 *
 * @param id ID of the Book
 * @return Returns [Item] object which can be null as well
 */
suspend fun getBookFromId(id: String): Item? {
    val retroService = RetrofitService.createRetroInterface(RetrofitService.getRetrofit(BASE_URL))
    return try {
        retroService.getBookFromId(id)
    } catch (e: Exception) {
        Log.d("bookfromid", "Cant get the book")
        null
    }
}

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
