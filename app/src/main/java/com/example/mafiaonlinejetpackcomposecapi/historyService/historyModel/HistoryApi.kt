package com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel

import com.example.mafiaonlinejetpackcomposecapi.HOST_1
import com.example.mafiaonlinejetpackcomposecapi.PORT_2
import com.example.mafiaonlinejetpackcomposecapi.historyService.utils.HistoryApiService
import com.example.mafiaonlinejetpackcomposecapi.okHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://$HOST_1:$PORT_2/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

object HistoryApi {
    val retrofitService: HistoryApiService by lazy {
        retrofit.create(HistoryApiService::class.java)
    }
}