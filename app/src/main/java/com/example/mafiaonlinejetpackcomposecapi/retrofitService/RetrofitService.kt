package com.example.mafiaonlinejetpackcomposecapi.retrofitService

import com.example.mafiaonlinejetpackcomposecapi.mainMenu_JoinRoomSection.RoomData
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_URL = "http://10.0.2.2:5020/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface MafiaApiService {
    @GET("update")
    suspend fun update(): List<RoomData>
    @POST("register")
    suspend fun register(@Body request: PlayerRegisterDataRequest): Int
    @POST("create")
    suspend fun createRoom(@Body request: CreateRoomRequest): Response<Unit>
    @GET("get-guid")
    suspend fun getGuid(): String
}

object MafiaApi {
    val retrofitService: MafiaApiService by lazy {
        retrofit.create(MafiaApiService::class.java)
    }
}

data class PlayerRegisterDataRequest(
    @SerializedName("Name") val nickname: String
)

//data class Player(
//    @SerializedName("PlayerId") val id: Int,

//    @SerializedName("role") val role: String?,
//    @SerializedName("IsAbilityAvailable") val isAbilityAvailable: Boolean,
//    @SerializedName("Password") val password: String
//)

data class CreateRoomRequest(
    @SerializedName("RoomName") val roomName: String = "",
    @SerializedName("PlayerName") val playerName: String,
    @SerializedName("MinPlayers") val minPlayers: Int = 5,
    @SerializedName("MaxPlayers") val maxPlayers: Int = 15,
//    @SerializedName("CreatorName") val creatorName: String = "Guest",
    @SerializedName("AllowedRoles") val allowedRoles: List<String> = listOf("MAFIA", "CIVILIAN"),
    @SerializedName("Password") val password: String = "",
//    @SerializedName("CurrentPlayers") var currentPlayers: List<Player> = emptyList()
)
