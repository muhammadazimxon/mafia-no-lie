package com.example.mafiaonlinejetpackcomposecapi.historyService.historyModel

import com.example.mafiaonlinejetpackcomposecapi.DOMAIN
import com.example.mafiaonlinejetpackcomposecapi.HOST_1
import com.example.mafiaonlinejetpackcomposecapi.PORT_2
import com.example.mafiaonlinejetpackcomposecapi.historyService.utils.HistoryApiService
import com.example.mafiaonlinejetpackcomposecapi.okHttpClient
import com.example.mafiaonlinejetpackcomposecapi.retrofitService.retrofitModel.retrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = DOMAIN

object HistoryApi {
    val retrofitService: HistoryApiService by lazy {
        retrofit.create(HistoryApiService::class.java)
    }
}