package com.karthihegde.readlist.di

import android.app.Application
import androidx.room.Room
import com.karthihegde.readlist.database.BookDatabase
import com.karthihegde.readlist.database.BookRepository
import com.karthihegde.readlist.retrofit.IRetrofit
import com.karthihegde.readlist.retrofit.RetroRepository
import com.karthihegde.readlist.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDatabase(app: Application): BookDatabase {
        return Room.databaseBuilder(
            app,
            BookDatabase::class.java,
            "book_data"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesRepository(db: BookDatabase): BookRepository {
        return BookRepository(db.bookDatabaseDo)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS).build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL).build()

    @Provides
    @Singleton
    fun providesRetroIn(retrofit: Retrofit): IRetrofit = retrofit.create(IRetrofit::class.java)

    @Provides
    @Singleton
    fun provideRetroRepo(iRetrofit: IRetrofit) = RetroRepository(iretrofit = iRetrofit)
}
