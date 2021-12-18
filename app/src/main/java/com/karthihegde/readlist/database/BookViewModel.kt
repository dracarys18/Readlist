package com.karthihegde.readlist.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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
