package com.example.mafiaonlinejetpackcomposecapi.gameRoom.components.playerCard

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.components.playerCard.parts.PlayerCardBackground
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.components.playerCard.parts.PlayerCardContent
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.components.playerCard.parts.getPlayerCardBackground
import com.example.mafiaonlinejetpackcomposecapi.gameRoom.gameRoomSignalRClient.Player
import com.example.mafiaonlinejetpackcomposecapi.roles.Role

data class PlayerCardParams(
    val player: Player,
    val currentPlayer: Player?,
    val isDead: Boolean = false,
    val isCardVotesVisible: Boolean,
    val isClickable: Boolean,
    val isKnownForPlayer: Boolean,
    val getPlayerVotes: Int,
    val onCardClick: () -> Unit
)

@Composable
fun PlayerCard(
    playerCardParams: PlayerCardParams
) {
    if (playerCardParams.currentPlayer == null || playerCardParams.player.playerRole == null || playerCardParams.currentPlayer.playerRole == null)
        return

    val backgroundBrush = getPlayerCardBackground(playerCardParams.currentPlayer, playerCardParams.player, playerCardParams.isKnownForPlayer, playerCardParams.isDead)
    val borderColor = if (playerCardParams.isDead) Color(0xFF660000) else Color(0xFF7B68EE)

    Card(
        modifier = Modifier
            .width(80.dp)
            .border(if (playerCardParams.isDead) 3.dp else 2.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable(enabled = playerCardParams.isClickable) { playerCardParams.onCardClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        PlayerCardBackground(backgroundBrush, playerCardParams.isDead) {
            PlayerCardContent(
                player = playerCardParams.player,
                currentPlayer = playerCardParams.currentPlayer,
                isDead = playerCardParams.isDead,
                isKnownForPlayer = playerCardParams.isKnownForPlayer,
                isCardVotesVisible = playerCardParams.isCardVotesVisible,
                votes = playerCardParams.getPlayerVotes
            )
        }
    }
}

@Preview
@Composable
fun PlayerCardPreview() {
    PlayerCard(
        PlayerCardParams(
            player = Player(playerRole = Role.Don),
            currentPlayer = Player(),
            isDead = false,
            isKnownForPlayer = false,
            getPlayerVotes = -1,
            isClickable = false,
            isCardVotesVisible = false,
            onCardClick = {}
        )
    )
}
