package com.leafcellteam.mafia.gameRoom.models

import androidx.compose.ui.graphics.Color
import com.leafcellteam.mafia.roles.Role
import kotlin.time.Clock
import kotlin.time.Instant

data class ChatMessageItemParams(
    val message: String = "",
    val player: Player? = null,
    val phase: Phase = Phase.DayDiscussion,
    val timeStamp: Instant = Clock.System.now(),
    val onSnapshotTextFirstLetter: (Phase, Player) -> String,
    val onSnapshotTextName: (Phase, Player) -> String,
    val onBackgroundSnapshot: (Phase, Role, Player) -> Color
)