package com.karthihegde.readlist.retrofit

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitService {
    fun getRetrofit(baseurl: String): Retrofit {
        return Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(baseurl).build()
    }

    fun createRetroInterface(retrofit: Retrofit): IRetrofit {
        return retrofit.create(IRetrofit::class.java)
    }
}