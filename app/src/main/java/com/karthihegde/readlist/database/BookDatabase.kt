package com.karthihegde.readlist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Abstract Class to connecting to Database
 */
@Database(entities = [BookData::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {
    abstract val bookDatabaseDo: BookDao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        /**
         * Get Database instance
         *
         * @param context
         * @return Database instance
         */
        fun getInstance(context: Context): BookDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BookDatabase::class.java,
                        "book_data"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
