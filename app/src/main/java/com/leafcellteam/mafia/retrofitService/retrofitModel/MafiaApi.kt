package com.leafcellteam.mafia.retrofitService.retrofitModel

import com.leafcellteam.mafia.DOMAIN
import com.leafcellteam.mafia.okHttpClient
import com.leafcellteam.mafia.retrofitService.utils.MafiaApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = DOMAIN

val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(DOMAIN)
    .client(okHttpClient)
    .build()


object MafiaApi {
    val retrofitService: MafiaApiService by lazy {
        retrofit.create(MafiaApiService::class.java)
    }
}