package com.leafcellteam.shared.gameRoom.models

enum class Phase {
    None, DayDiscussion, DayVote, NightDiscussion, NightVote
}

fun Phase.parseToPhase(phase: String): Phase {
    return when (phase) {
        "DayDiscussion" -> Phase.DayDiscussion
        "DayVote" -> Phase.DayVote
        "NightDiscussion" -> Phase.NightDiscussion
        "NightVote" -> Phase.NightVote
        else -> Phase.None
    }
}
