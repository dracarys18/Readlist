package com.karthihegde.readlist.retrofit

import BASE_URL
import android.util.Log
import com.karthihegde.readlist.booklist

suspend fun getBookFromSearch(query: String) {
    val retroservice = RetrofitService.createRetroInterface(RetrofitService.getRetrofit(BASE_URL))
    try {
        booklist.value = retroservice.getBooks(query)
    } catch (e: Exception) {
        Log.d("bookfromsearch", "Some problem with retrofit")
    }
}