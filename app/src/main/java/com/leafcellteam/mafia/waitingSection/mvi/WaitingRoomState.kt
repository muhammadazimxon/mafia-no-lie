package com.leafcellteam.mafia.waitingSection.mvi

import com.leafcellteam.mafia.waitingSection.waitingRoomModels.ChatMessageData
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.SystemMessage
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.UserColor
import com.leafcellteam.mafia.waitingSection.waitingRoomModels.WaitingRoomDto

data class WaitingRoomState(
    val guid: String = "",
    val roomDto: WaitingRoomDto? = null,
    val messages: List<ChatMessageData>? = null,
    val systemMessage: List<SystemMessage>? = null,
    val playerId: Int = 0,
    val gameCounter: Int = 25,
    val userColors: List<UserColor> = emptyList(),
    val isBackState: Boolean = false,
    val backHandlerEnabled: Boolean = false,
    val messageDraft: String = "",
    val counter: Int = 25
)