package com.example.currency.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        private const val AccessKey = "9abb91b2a12547fb4374bd9c7746844e"
    }

    @GET("symbols")
    suspend fun getSymbols(
        @Query("access_key") access_key: String = AccessKey,
    ): Response<SymbolsResponse>


    @GET("convert")
    suspend fun convert(
        @Query("access_key") access_key: String = AccessKey,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: String,
    ): Response<ConvertResponse>

    @GET("latest")
    suspend fun latest(
        @Query("access_key") access_key: String = AccessKey,
        @Query("base") base: String,
        @Query("symbols") symbols: String,
    ): Response<LatestResponse>

}



