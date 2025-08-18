package com.example.mafiaonlinejetpackcomposecapi.gameRoom.components.playerCard.parts

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameRoomSignalRClient.Player
import com.example.mafiaonlinejetpackcomposecapi.roles.Role

fun getPlayerCardBackground(
    currentPlayer: Player,
    player: Player,
    knownForPlayer: Boolean,
    isDead: Boolean
): Brush {
    return when {
        currentPlayer.playerId == player.playerId -> {
            if(player.playerRole!!.category == Role.Category.CIVILIAN)
                Brush.radialGradient(listOf(Color(0xFF6F8B00), Color(0xFF605501), Color(0xFF214001)))
            else
                Brush.radialGradient(listOf(Color(0xFF6F8B00), Color(0xFF605501), Color(0xFF401D01)))
        }

        isDead -> Brush.radialGradient(
            colors = listOf(Color(0xFF8B0000), Color(0xFF450000), Color(0xFF1A0000)),
            radius = 120f
        )

        currentPlayer.playerRole is Role.Mimic && (player.playerRole is Role.Don || player.playerRole is Role.Mafia) -> Brush.verticalGradient(
            listOf(Color(0x33FF0000), Color(0x22FF0000))
        )

        knownForPlayer && player.playerRole!!.category == Role.Category.MAFIA -> Brush.verticalGradient(
            listOf(Color(0x33FF0000), Color(0x22FF0000))
        )

        knownForPlayer && player.playerRole!!.category == Role.Category.CIVILIAN -> Brush.verticalGradient(
            listOf(Color(0x3337FF00), Color(0x2222FF00))
        )

        else -> Brush.verticalGradient(
            listOf(Color(0x33777777), Color(0x22555555))
        )
    }
}
