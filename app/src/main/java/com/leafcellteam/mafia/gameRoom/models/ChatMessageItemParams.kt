package com.leafcellteam.mafia.gameRoom.models

import androidx.compose.ui.graphics.Color
import com.leafcellteam.mafia.roles.Role
import java.util.Date

data class ChatMessageItemParams(
    val message: String = "",
    val player: Player? = null,
    val phase: Phase = Phase.DayDiscussion,
    val timeStamp: Date = Date(),
    val onSnapshotTextFirstLetter: (Phase, Player) -> String,
    val onSnapshotTextName: (Phase, Player) -> String,
    val onBackgroundSnapshot: (Phase, Role, Player) -> Color
)