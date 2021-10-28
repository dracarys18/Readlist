package com.karthihegde.readlist.retrofit

import com.karthihegde.readlist.retrofit.data.BookList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IRetrofit {
    @GET("volumes")
    suspend fun getBooks(@Query("q") query: String): BookList
}