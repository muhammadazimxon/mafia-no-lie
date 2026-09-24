package com.leafcellteam.mafia.serializableData

import com.leafcellteam.mafia.serializableData.route_interface.Route
import kotlinx.serialization.Serializable

@Serializable
data object JoinGameScreen : Route { override val route = "JoinGameScreen" }