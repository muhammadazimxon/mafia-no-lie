package com.leafcellteam.shared.navigation

import com.leafcellteam.shared.navigation.route.Route
import kotlinx.serialization.Serializable

@Serializable
data object EnterEmailScreen : Route { override val route = "EnterEmailScreen" }
