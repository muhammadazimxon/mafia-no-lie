package com.leafcellteam.shared.navigation

import com.leafcellteam.shared.navigation.route.Route
import kotlinx.serialization.Serializable

@Serializable
data object CreateGameScreen : Route { override val route = "CreateGameScreen" }
