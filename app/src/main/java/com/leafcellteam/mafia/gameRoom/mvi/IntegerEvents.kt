package com.leafcellteam.mafia.gameRoom.mvi

sealed class IntegerEvents
    data class GetPlayerVotes(val id: Int): IntegerEvents()
