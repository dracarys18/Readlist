package com.karthihegde.readlist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: BookData)

    @Query("DELETE FROM BOOK_DATA WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM BOOK_DATA")
    fun getAllBooks(): LiveData<List<BookData>>

    @Query("SELECT * FROM BOOK_DATA WHERE ID = :id")
    fun getBookFromId(id: String): LiveData<BookData>

}
