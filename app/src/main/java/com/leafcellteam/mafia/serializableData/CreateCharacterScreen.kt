package com.leafcellteam.mafia.serializableData

import com.leafcellteam.mafia.serializableData.route_interface.Route
import kotlinx.serialization.Serializable

@Serializable
data object CreateCharacterScreen : Route { override val route = "CreateCharacterScreen" }