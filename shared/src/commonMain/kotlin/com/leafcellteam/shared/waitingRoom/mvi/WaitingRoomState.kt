package com.leafcellteam.shared.waitingRoom.mvi

import com.leafcellteam.shared.waitingRoom.models.ChatMessageData
import com.leafcellteam.shared.waitingRoom.models.SystemMessage
import com.leafcellteam.shared.waitingRoom.models.WaitingRoomDto

data class WaitingRoomState(
    val guid: String = "",
    val roomDto: WaitingRoomDto? = null,
    val messages: List<ChatMessageData>? = null,
    val systemMessage: List<SystemMessage>? = null,
    val playerId: Int = 0,
    val gameCounter: Int = 25,
    val userColors: List<com.leafcellteam.shared.waitingRoom.models.UserColor> = emptyList(),
    val isBackState: Boolean = false,
    val backHandlerEnabled: Boolean = false,
    val messageDraft: String = "",
    val counter: Int = 25
)
