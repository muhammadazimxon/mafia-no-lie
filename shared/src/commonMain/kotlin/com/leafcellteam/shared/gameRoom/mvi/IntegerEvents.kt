package com.leafcellteam.shared.gameRoom.mvi

sealed class IntegerEvents
    data class GetPlayerVotes(val id: Int): IntegerEvents()
