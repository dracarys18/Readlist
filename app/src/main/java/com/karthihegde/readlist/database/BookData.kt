package com.karthihegde.readlist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data Class for Table
 *
 * @param id Book ID
 * @param bookName Book Name
 * @param author Book Author
 * @param imageUrl Book Thumbnail URL
 * @param totalPages Total Pages of the Book
 * @param pagesRead Total Pages Read in the Book
 * @param insertDate Book Insertion Date
 */
@Entity(tableName = "book_data")
data class BookData(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "book_name") val bookName: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "total_pages") val totalPages: Int,
    @ColumnInfo(name = "pages_read") val pagesRead: Int = 0,
    @ColumnInfo(name = "insert_date") val insertDate: String,
)

