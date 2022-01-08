package com.karthihegde.readlist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karthihegde.readlist.database.BookData
import com.karthihegde.readlist.database.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Database
 */
@HiltViewModel
class BookViewModel
@Inject constructor(
    private val repository: BookRepository
) : ViewModel() {
    val getAllData: Flow<List<BookData>> = repository.readBookData
    val groupByStatus: Flow<Map<String, List<BookData>>> = repository.groupByStatus

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

