package com.leafcellteam.shared.navigation

import com.leafcellteam.shared.navigation.route.Route
import kotlinx.serialization.Serializable

@Serializable
object ProfileScreen : Route {
    override val route: String = "ProfileScreen"
}
