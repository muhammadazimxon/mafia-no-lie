package com.leafcellteam.shared.gameRoom.models

import com.leafcellteam.shared.waitingRoom.models.ChatMessage

data class GameRoomState(
    val guid: String = "",
    val timer: Int = -1,
    val mafiaQuantity: Int = 0,
    val civilianQuantity: Int = 0,
    val totalAlivePlayers: Int = 0,
    val phase: Phase = Phase.None,
    val gameEvents: List<String> = emptyList(),
    val messages: List<ChatMessage> = emptyList(),
    val mafiaMessages: List<ChatMessage> = emptyList(),
    val oldPlayers: List<Player> = emptyList(),
    var alivePlayers: List<AlivePlayersTransmission> = emptyList(),
    var playersToVote: VotePlayers = VotePlayers(emptyMap()),
    val isGameEnd: Boolean = false,
    val endGameMessage: String = ""
)
