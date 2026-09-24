package com.leafcellteam.mafia.gameRoom.models

import kotlinx.serialization.Serializable

@Serializable
data class PlayerDto(
    val playerId: Int = 0,
    val playerName: String = "",
    val playerRole: String = "",
    val avatarColor: Array<Int> = emptyArray<Int>() // TODO  !!! IntArray better
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other) return false

        other as PlayerDto

        if (playerId != other.playerId) return false
        if (playerName != other.playerName) return false
        if (playerRole != other.playerRole) return false
        if (!avatarColor.contentEquals(other.avatarColor)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = playerId
        result = 31 * result + playerName.hashCode()
        result = 31 * result + playerRole.hashCode()
        result = 31 * result + avatarColor.contentHashCode()
        return result
    }
}