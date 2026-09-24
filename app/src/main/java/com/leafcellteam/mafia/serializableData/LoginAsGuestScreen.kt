package com.leafcellteam.mafia.serializableData

import com.leafcellteam.mafia.serializableData.route_interface.Route
import kotlinx.serialization.Serializable

@Serializable
object LoginAsGuestScreen : Route { override val route = "LoginAsGuestScreen" }