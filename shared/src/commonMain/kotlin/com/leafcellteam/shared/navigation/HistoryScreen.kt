package com.leafcellteam.shared.navigation

import com.leafcellteam.shared.navigation.route.Route
import kotlinx.serialization.Serializable

@Serializable
data object HistoryScreen : Route { override val route = "HistoryScreen" }
