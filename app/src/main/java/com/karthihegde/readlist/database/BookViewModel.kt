package com.karthihegde.readlist.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow

/**
 * ViewModel for the Database
 *
 * @param application Application Context
 */
class BookViewModel(application: Application) : AndroidViewModel(application) {
    val getAllData: Flow<List<BookData>>
    val dao: BookDao = BookDatabase.getInstance(application).bookDatabaseDo

    init {
        getAllData = dao.getAllBooks()
    }
}

class BookViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            return BookViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
