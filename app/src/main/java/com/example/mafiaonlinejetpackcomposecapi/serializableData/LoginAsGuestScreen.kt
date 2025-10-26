package com.example.mafiaonlinejetpackcomposecapi.serializableData

import com.example.mafiaonlinejetpackcomposecapi.serializableData.route_interface.Route
import kotlinx.serialization.Serializable

@Serializable
object LoginAsGuestScreen : Route { override val route = "LoginAsGuestScreen" }