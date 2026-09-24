package com.leafcellteam.mafia.serializableData

class GameRoomScreen {
    companion object {
        const val ROUTE = "GameRoomScreen"
        private val _fromScreen : FromScreenData = FromScreenData(WaitingRoomScreen.getFromScreen())
        fun getFromScreen() : FromScreenData = _fromScreen
    }
}