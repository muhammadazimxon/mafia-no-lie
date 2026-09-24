package com.leafcellteam.mafia.retrofitService.retrofitModel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    @SerialName("RoomName") val roomName: String = "",
    @SerialName("PlayerName") val playerName: String,
    @SerialName("MinPlayers") val minPlayers: Int = 5,
    @SerialName("MaxPlayers") val maxPlayers: Int = 15,
    @SerialName("AllowedRoles") val allowedRoles: List<String> = /*listOf("MAFIA", "CIVILIAN")*/ listOf(),
    @SerialName("Password") val password: String = "",
    @SerialName("SelectedPhase") val phase: String = "",
    @SerialName("Country") val country: String = ""
)