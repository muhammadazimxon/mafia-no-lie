package com.leafcellteam.mafia.serializableData

import com.leafcellteam.mafia.serializableData.fromScreen.FromScreen

class WaitingRoomScreen {
    companion object {
        private var _fromScreen : FromScreenData? = null
        const val ROUTE = "WaitingRoomScreen"

        fun setFromScreen(from: FromScreenData) {
            _fromScreen = from
        }
        
        fun getFromScreen() = _fromScreen!!.value
    }
}

data class FromScreenData(val value: FromScreen)