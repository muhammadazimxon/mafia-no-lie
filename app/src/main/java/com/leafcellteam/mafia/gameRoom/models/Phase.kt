package com.leafcellteam.mafia.gameRoom.models

enum class Phase {
    NightDiscussion, NightVote, DayDiscussion, DayVote, None;

    fun parseToPhase(phase: String): Phase {
        return when(phase) {
            "NightDiscussion" -> NightDiscussion
            "NightVote" -> NightVote
            "DayDiscussion" -> DayDiscussion
            "DayVote" -> DayVote
            else -> DayVote
        }
    }
}