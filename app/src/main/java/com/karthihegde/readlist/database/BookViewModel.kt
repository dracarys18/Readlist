package com.karthihegde.readlist.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class BookViewModel(application: Application) : AndroidViewModel(application) {
    val getAllData: LiveData<List<BookData>>
    val dao: BookDao = BookDatabase.getInstance(application).bookDatabaseDo

    init {
        getAllData = dao.getAllBooks()
    }
}
