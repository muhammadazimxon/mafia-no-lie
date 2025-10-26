package com.example.mafiaonlinejetpackcomposecapi.serializableData

import com.example.mafiaonlinejetpackcomposecapi.serializableData.WaitingRoomScreen.Companion

class GameRoomScreen {
    companion object {
        const val ROUTE = "GameRoomScreen"
        private val _fromScreen : FromScreenData = FromScreenData(WaitingRoomScreen.getFromScreen())
        fun getFromScreen() : FromScreenData = _fromScreen
    }
}