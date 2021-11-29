package com.karthihegde.readlist.retrofit

import com.karthihegde.readlist.retrofit.data.BookList
import com.karthihegde.readlist.retrofit.data.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit Interface
 */
interface IRetrofit {
    /**
     * Get Books from Network Request
     *
     * @param query Search Query to search
     * @param maxResults Maximum amount of elements to be returned
     * @return [BookList]
     */
    @GET("volumes")
    suspend fun getBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 40
    ): BookList

    /**
     * Get Books from ID
     *
     * @param id Book ID
     * @return [Item]
     */
    @GET("volumes/{id}")
    suspend fun getBookFromId(@Path("id") id: String): Item
}
