package com.leafcellteam.shared.gameRoom.mvi

import com.leafcellteam.shared.gameRoom.models.Player
import com.leafcellteam.shared.roles.Role

sealed class BooleanEvents
    data class IsPlayerCardClick(
        val currentPlayerId: Int,
        val playerId: Int,
        val isDead: Boolean,
        val currentPlayerRole: Role,
        val playerRole: Role
    ) : BooleanEvents()

    data class IsExitOrObserveDialogShown(
        val currentPlayerId: Int,
        val playerId: Int,
        val isDead: Boolean
    ) : BooleanEvents()

    data class IsCardVotesVisible(val votes: Int, val playerRole: Role) : BooleanEvents()

    data class BarmanHandler(val currentPlayer: Player) : BooleanEvents()

    data class JournalistHandler(val selectedPlayerId: Int, val currentPlayer: Player) : BooleanEvents()

    data class DetectiveHandler(val selectedPlayerId: Int, val currentPlayer: Player) : BooleanEvents()

    data class InformatorHandler(val selectedPlayerId: Int, val currentPlayer: Player) : BooleanEvents()

    data class IsDoubleDialog(val selectedPlayerId: Int) : BooleanEvents()

    data class IsBomberOnlyAbilityUse(val currentPlayerRole: Role) : BooleanEvents()

    data object IsMeNotUnderLover : BooleanEvents()

    data class DonHandler(val selectedPlayerId: Int, val currentPlayer: Player, val isDead: Boolean) : BooleanEvents()

    data class IsTextFieldPermitted(val currentPlayer: Player?) : BooleanEvents()
