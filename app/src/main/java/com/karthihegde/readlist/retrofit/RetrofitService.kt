package com.karthihegde.readlist.retrofit

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Retrofit Service Object
 */
object RetrofitService {
    /**
     * Get retrofit object
     *
     * @param baseurl URL to Request
     * @return [Retrofit] Retrofit Object
     */
    fun getRetrofit(baseurl: String): Retrofit {
        return Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(baseurl).build()
    }

    /**
     * Create retrofit Interface
     *
     * @param retrofit Retrofit object
     * @return [IRetrofit] Retrofit interface
     */
    fun createRetroInterface(retrofit: Retrofit): IRetrofit {
        return retrofit.create(IRetrofit::class.java)
    }
}
