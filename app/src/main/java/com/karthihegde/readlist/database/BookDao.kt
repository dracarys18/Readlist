package com.karthihegde.readlist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Book DAO for Room Database
 */
@Dao
interface BookDao {
    /**
     * Insert function for Room Database
     *
     * @param data BookData Object from [BookData]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: BookData)

    /**
     * Delete SQL statement
     *
     * @param id Book ID
     */
    @Query("DELETE FROM BOOK_DATA WHERE id = :id")
    suspend fun delete(id: String)

    /**
     * Get all books as LiveData from Database
     */
    @Query("SELECT * FROM BOOK_DATA")
    fun getAllBooks(): Flow<List<BookData>>

    /**
     * Get Book From ID
     *
     * @param id Book Id
     * @return BookData object as LiveData from Database
     */
    @Query("SELECT * FROM BOOK_DATA WHERE ID = :id")
    fun getBookFromId(id: String): Flow<BookData>

    /**
     * Check if Book exist or Not
     *
     * @param id Book ID
     * @return book exist or not as LiveData
     */
    @Query("SELECT EXISTS (SELECT 1 FROM book_data WHERE id = :id)")
    fun checkIfBookExists(id: String): Flow<Boolean>

    /**
     * Get the number of Pages Read
     *
     * @param id Book ID
     * @return Pages read
     */
    @Query("SELECT pages_read FROM book_data WHERE id = :id")
    fun getPagesRead(id: String): Flow<Int>

    /**
     * Update the pages read
     *
     * @param pages new page value
     * @param id Book Id
     */
    @Query("UPDATE book_data SET pages_read=:pages where id = :id")
    suspend fun updatePages(pages: Int, id: String)
}
