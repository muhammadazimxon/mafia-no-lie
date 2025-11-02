package com.example.mafiaonlinejetpackcomposecapi.serializableData

import com.example.mafiaonlinejetpackcomposecapi.serializableData.route_interface.Route
import kotlinx.serialization.Serializable

@Serializable
data object CreateCharacterScreen : Route { override val route = "CreateCharacterScreen" }