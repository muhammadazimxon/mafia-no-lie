package com.leafcellteam.shared.gameRoom.models

import com.leafcellteam.shared.roles.Role

data class ChatMessageItemParams(
    val message: String = "",
    val player: Player? = null,
    val phase: Phase = Phase.DayDiscussion,
    val timeStampLong: Long = 0L,
    val onSnapshotTextFirstLetter: (Phase, Player) -> String,
    val onSnapshotTextName: (Phase, Player) -> String,
    val onBackgroundSnapshot: (Phase, Role, Player) -> Long
)
