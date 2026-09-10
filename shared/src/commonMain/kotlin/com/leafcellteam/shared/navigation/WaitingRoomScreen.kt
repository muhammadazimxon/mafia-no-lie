package com.leafcellteam.shared.navigation

import com.leafcellteam.shared.navigation.fromScreen.FromScreen

data class FromScreenData(val value: FromScreen)

class WaitingRoomScreen {
    companion object {
        private var _fromScreen: FromScreenData? = null
        const val ROUTE = "WaitingRoomScreen"

        fun setFromScreen(from: FromScreenData) {
            _fromScreen = from
        }

        fun getFromScreen() = _fromScreen!!.value
    }
}
