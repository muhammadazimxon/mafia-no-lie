package com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi

import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameRoomSignalRClient.Phase
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameRoomSignalRClient.Player

sealed class GameEvents {
    data class SnapshotTextFirstLetter(val snapshotPhase: Phase, val currentPlayer: Player?, val itPlayer: Player) : GameEvents()
    data class SnapshotTextName(val snapshotPhase: Phase, val currentPlayer: Player?, val itPlayer: Player) : GameEvents()
}