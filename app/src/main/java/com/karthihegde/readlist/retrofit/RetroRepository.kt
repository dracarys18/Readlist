package com.karthihegde.readlist.retrofit

import com.karthihegde.readlist.retrofit.data.BookList
import com.karthihegde.readlist.retrofit.data.Item
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class RetroRepository @Inject constructor(
    private val iretrofit: IRetrofit
) {
    suspend fun getBooks(query: String): RetroState<BookList> {
        return try {
            RetroState.Success(data = iretrofit.getBooks(query = query))
        } catch (e: Exception) {
            RetroState.Failure(message = e.message)
        }
    }

    suspend fun getBooksFromId(id: String): RetroState<Item> {
        return try {
            RetroState.Success(data = iretrofit.getBookFromId(id = id))
        } catch (e: Exception) {
            RetroState.Failure(message = e.message)
        }
    }
}
