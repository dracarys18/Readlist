package com.karthihegde.readlist.retrofit

import BASE_URL
import android.util.Log
import com.karthihegde.readlist.book
import com.karthihegde.readlist.booklist
import java.util.*

suspend fun getBookFromSearch(query: String) {
    val retroservice = RetrofitService.createRetroInterface(RetrofitService.getRetrofit(BASE_URL))
    try {
        booklist.value = retroservice.getBooks(query)
    } catch (e: Exception) {
        Log.d("bookfromsearch", "Some problem with retrofit")
    }
}

suspend fun getBookFromId(id: String) {
    val retroservice = RetrofitService.createRetroInterface(RetrofitService.getRetrofit(BASE_URL))
    try {
        book.value = retroservice.getBookFromId(id)
    } catch (e: Exception) {
        Log.d("bookfromid", "Cant get the book")
    }
}

fun getCurrencySymbol(code: String?): String? {
    val cur = Currency.getInstance(code)
    return cur.symbol
}
