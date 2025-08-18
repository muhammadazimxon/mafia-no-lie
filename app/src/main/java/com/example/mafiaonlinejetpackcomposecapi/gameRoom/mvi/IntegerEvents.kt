package com.example.mafiaonlinejetpackcomposecapi.gameRoom.mvi

sealed class IntegerEvents
    data class GetPlayerVotes(val id: Int): IntegerEvents()
