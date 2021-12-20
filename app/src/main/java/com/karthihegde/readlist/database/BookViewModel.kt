package com.karthihegde.readlist.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Database
 *
 * @param application Application Context
 */
class BookViewModel(application: Application) : AndroidViewModel(application) {
    val getAllData: Flow<List<BookData>>
    private val repository: BookRepository

    init {
        val dao = BookDatabase.getInstance(application).bookDatabaseDo
        repository = BookRepository(dao)
        getAllData = repository.readBookData
    }

    /**
     * Insert function that's exposed to use outside
     *
     * @param bookData
     */
    fun insert(bookData: BookData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(bookData)
        }
    }

    /**
     * Delete function that's exposed to use outside
     *
     * @param id
     */
    fun delete(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(id)
        }
    }

    /**
     * Update pages function that's exposed to use outside
     *
     * @param pages
     * @param id
     */
    fun updatePages(pages: Int, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePages(pages = pages, id = id)
        }
    }

    /**
     * Get BookData object from data function that's exposed to use outside
     *
     * @param id
     */
    fun getBookFromId(id: String): Flow<BookData> {
        return repository.getBookFromId(id = id)
    }

    /**
     * Check if book exists or not function that's exposed to use outside
     *
     * @param id
     */
    fun checkIfBookExists(id: String): Flow<Boolean> {
        return repository.checkIfBookExists(id = id)
    }

    /**
     * GetPages that's exposed to use outside
     *
     * @param id
     */
    fun getPagesRead(id: String): Flow<Int> {
        return repository.getPagesRead(id = id)
    }
}

/**
 * VieModelFactory class which is used to create ViewModel with arguments
 *
 * @param application Application context
 */
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
