package com.karthihegde.readlist.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

/**
 * ViewModel for the Database
 *
 * @param application Application Context
 */
class BookViewModel(application: Application) : AndroidViewModel(application) {
    val getAllData: LiveData<List<BookData>>
    val dao: BookDao = BookDatabase.getInstance(application).bookDatabaseDo

    init {
        getAllData = dao.getAllBooks()
    }
}
