package com.karthihegde.readlist.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Abstract Class to connecting to Database
 */
@Database(entities = [BookData::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {
    abstract val bookDatabaseDo: BookDao
}
