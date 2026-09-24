package com.leafcellteam.mafia.retrofitService.retrofitModel

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    @SerializedName("RoomName") val roomName: String = "",
    @SerializedName("PlayerName") val playerName: String,
    @SerializedName("MinPlayers") val minPlayers: Int = 5,
    @SerializedName("MaxPlayers") val maxPlayers: Int = 15,
    @SerializedName("AllowedRoles") val allowedRoles: List<String> = /*listOf("MAFIA", "CIVILIAN")*/ listOf(),
    @SerializedName("Password") val password: String = "",
    @SerializedName("SelectedPhase") val phase: String = "",
    @SerializedName("Country") val country: String = ""
)