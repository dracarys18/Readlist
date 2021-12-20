package com.karthihegde.readlist.database

import kotlinx.coroutines.flow.Flow

/**
 * Repository Class for Book Database
 *
 * @param bookDatabaseDao Book Database Dao
 */
class BookRepository(private val bookDatabaseDao: BookDao) {

    val readBookData: Flow<List<BookData>> = bookDatabaseDao.getAllBooks()

    /**
     * Abstract insert function
     *
     * @param bookData
     */
    suspend fun insert(bookData: BookData) {
        bookDatabaseDao.insert(bookData)
    }

    /**
     * Abstract delete function
     *
     * @param id
     */
    suspend fun delete(id: String) {
        bookDatabaseDao.delete(id)
    }

    /**
     * Abstract UpdatePages function
     *
     * @param pages
     * @param id
     */
    suspend fun updatePages(pages: Int, id: String) {
        bookDatabaseDao.updatePages(pages = pages, id = id)
    }

    /**
     * Abstract [getBookFromId]
     *
     * @param id
     */
    fun getBookFromId(id: String): Flow<BookData> {
        return bookDatabaseDao.getBookFromId(id = id)
    }

    /**
     * Abstract [checkIfBookExists] function
     *
     * @param id
     */
    fun checkIfBookExists(id: String): Flow<Boolean> {
        return bookDatabaseDao.checkIfBookExists(id = id)
    }

    /**
     * Abstract [getPagesRead] function
     *
     * @param id
     */
    fun getPagesRead(id: String): Flow<Int> {
        return bookDatabaseDao.getPagesRead(id = id)
    }

}
