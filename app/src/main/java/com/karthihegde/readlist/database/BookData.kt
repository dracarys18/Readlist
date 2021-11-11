package com.karthihegde.readlist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_data")
data class BookData(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "book_name") val bookName: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "pages_read") val pagesRead: Int = 0
)

