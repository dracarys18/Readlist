package com.karthihegde.readlist.utils

import android.util.Log
import com.karthihegde.readlist.retrofit.RetrofitService
import com.karthihegde.readlist.retrofit.data.Item
import com.karthihegde.readlist.ui.SearchResults
import java.util.*

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

suspend fun getBookFromId(id: String): Item? {
    val retroService = RetrofitService.createRetroInterface(RetrofitService.getRetrofit(BASE_URL))
    return try {
        retroService.getBookFromId(id)
    } catch (e: Exception) {
        Log.d("bookfromid", "Cant get the book")
        null
    }
}

fun getCurrencySymbol(code: String?): String? {
    val cur = Currency.getInstance(code)
    return cur.symbol
}
