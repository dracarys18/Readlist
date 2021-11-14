package com.karthihegde.readlist.retrofit

import com.karthihegde.readlist.retrofit.data.BookList
import com.karthihegde.readlist.retrofit.data.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IRetrofit {
    @GET("volumes")
    suspend fun getBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 40
    ): BookList

    @GET("volumes/{id}")
    suspend fun getBookFromId(@Path("id") id: String): Item
}
