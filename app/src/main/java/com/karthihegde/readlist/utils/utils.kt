package com.karthihegde.readlist.utils

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.karthihegde.readlist.retrofit.RetrofitService
import com.karthihegde.readlist.retrofit.data.Item
import java.util.*

suspend fun getBookFromSearch(query: String) {
    val retroservice = RetrofitService.createRetroInterface(RetrofitService.getRetrofit(BASE_URL))
    try {
        booklist.value = retroservice.getBooks(query)
    } catch (e: Exception) {
        Log.d("bookfromsearch", "Some problem with retrofit")
    }
}

suspend fun getBookFromId(id: String): Item? {
    val retroservice = RetrofitService.createRetroInterface(RetrofitService.getRetrofit(BASE_URL))
    return try {
        retroservice.getBookFromId(id)
    } catch (e: Exception) {
        Log.d("bookfromid", "Cant get the book")
        null
    }
}

fun getCurrencySymbol(code: String?): String? {
    val cur = Currency.getInstance(code)
    return cur.symbol
}
