package com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel

import com.example.mafiaonlinejetpackcomposecapi.DOMAIN
import com.example.mafiaonlinejetpackcomposecapi.HOST_1
import com.example.mafiaonlinejetpackcomposecapi.PORT_2
import com.example.mafiaonlinejetpackcomposecapi.okHttpClient
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.utils.MafiaApiService
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