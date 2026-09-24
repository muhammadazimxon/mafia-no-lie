package com.leafcellteam.mafia.gameRoom.mvi

import com.leafcellteam.mafia.gameRoom.models.Phase
import com.leafcellteam.mafia.gameRoom.models.Player

sealed class GameEvents {
    data class SnapshotTextFirstLetter(val snapshotPhase: Phase, val currentPlayer: Player?, val itPlayer: Player) : GameEvents()
    data class SnapshotTextName(val snapshotPhase: Phase, val currentPlayer: Player?, val itPlayer: Player) : GameEvents()
}